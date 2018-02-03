package pl.marko.logback.composite.loggingevent;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class MdcJsonNestedObjectProvider extends AbstractMdcJsonProvider {

    private final static String NESTED_OBJECT_SEPARATOR = ".";
    private static final String NESTED_OBJECT_SEPARATOR_REGEX = "\\.";
    private boolean mapToNestedObjects = false;


    private TreeNode convertToNestedObjects(Map<String, String> properties) {

        Map<String, LinkedList<String>> nestedObjectProperties = properties.keySet().stream()
                .filter(key -> key.contains(NESTED_OBJECT_SEPARATOR))
                .collect(Collectors.toMap(key -> key, key -> new LinkedList<>(asList(key.split(NESTED_OBJECT_SEPARATOR_REGEX)))));

        return groupByPath(nestedObjectProperties);
    }

    private TreeNode groupByPath(Map<String, LinkedList<String>> nestedObjectProperties) {
        TreeNode root = new TreeNode(null, null);
        nestedObjectProperties.forEach((pathString,pathList) -> buildTree(root, pathString, pathList));
        return root;
    }

    @Override
    void writeProperties(JsonGenerator generator, Map<String, String> mdcProperties) throws IOException {
        writeTree(generator, convertToNestedObjects(mdcProperties), mdcProperties);
    }


    private void buildTree(TreeNode tree, String pathString, List<String> path) {
        if (path != null && !path.isEmpty()) {
            String head = path.get(0);
            List<String> tail = path.subList(1, path.size());
            if (tree.children.containsKey(head)) {
                buildTree(tree.children.get(head), pathString, tail);
            } else {
                TreeNode child = new TreeNode(head, pathString);
                tree.children.put(head, child);
                buildTree(child, pathString, tail);
            }
        }
    }

    private void writeTree(JsonGenerator generator, TreeNode tree, Map<String, String> mdcProperties) throws IOException {
        Map<String, TreeNode> children = tree.children;
        for (Map.Entry<String, TreeNode> child : children.entrySet()) {
            String propertyKey = child.getKey();
            TreeNode childNode = child.getValue();
            String fullPath = childNode.fullPath;
            if(!childNode.children.isEmpty()) {
                generator.writeFieldName(propertyKey);
                generator.writeStartObject();
                writeTree(generator, childNode, mdcProperties);
                generator.writeEndObject();
            } else {
                generator.writeFieldName(propertyKey);
                generator.writeObject(mdcProperties.get(fullPath));
            }
        }
    }

    public boolean isMapToNestedObjects() {
        return mapToNestedObjects;
    }

    public void setMapToNestedObjects(boolean mapToNestedObjects) {
        this.mapToNestedObjects = mapToNestedObjects;
    }


    private static class TreeNode {
        String name;
        Map<String, TreeNode> children = new HashMap<>();
        String fullPath;

        TreeNode(String name, String fullPath) {
            this.name = name;
            this.fullPath = fullPath;
        }
    }
}