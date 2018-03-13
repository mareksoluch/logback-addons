package pl.marko.logback.composite.loggingevent;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class MdcJsonNestedObjectProvider extends AbstractMdcJsonProvider {

    private static final String NESTED_OBJECT_SEPARATOR_REGEX = "\\.";
    private boolean printNestedObjects;

    public MdcJsonNestedObjectProvider() {
        this(true);
    }

    public MdcJsonNestedObjectProvider(boolean printNestedObjects) {
        this.printNestedObjects = printNestedObjects;
    }

    @Override
    void writeProperties(JsonGenerator generator, Map<String, String> mdcProperties) throws IOException {
        writeTree(generator, convertToNestedObjects(mdcProperties), mdcProperties);
        generator.flush();
    }

    private List<String> getPath(String key) {
        return printNestedObjects
                ? new LinkedList<>(asList(key.split(NESTED_OBJECT_SEPARATOR_REGEX)))
                : singletonList(key);
    }

    private TreeNode groupByPath(Map<String, List<String>> nestedObjectProperties) {
        TreeNode root = new TreeNode(null);
        nestedObjectProperties.forEach((pathString,pathList) -> buildTree(root, pathString, pathList));
        return root;
    }

    private TreeNode convertToNestedObjects(Map<String, String> properties) {

        Map<String, List<String>> nestedObjectProperties = properties.keySet().stream()
                .collect(toMap(identity(), this::getPath));

        return groupByPath(nestedObjectProperties);
    }


    private void buildTree(TreeNode tree, String pathString, List<String> path) {
        if (path != null && !path.isEmpty()) {
            String head = path.get(0);
            List<String> tail = path.subList(1, path.size());
            if (tree.children.containsKey(head)) {
                buildTree(tree.children.get(head), pathString, tail);
            } else {
                TreeNode child = new TreeNode(pathString);
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
            if(!childNode.children.isEmpty()) {
                generator.writeObjectFieldStart(propertyKey);
                writeTree(generator, childNode, mdcProperties);
                generator.writeEndObject();
            } else {
                writeJsonValue(generator, propertyKey, mdcProperties.get(childNode.fullPath));
            }
        }
    }

    protected void writeJsonValue(JsonGenerator generator, String jsonName, String jsonValue) throws IOException {
        generator.writeObjectField(jsonName, jsonValue);
    }

    public boolean isPrintNestedObjects() {
        return printNestedObjects;
    }

    public void setPrintNestedObjects(boolean printNestedObjects) {
        this.printNestedObjects = printNestedObjects;
    }


    private class TreeNode {
        private final Map<String, TreeNode> children = new HashMap<>();
        private final String fullPath;

        TreeNode(String fullPath) {
            this.fullPath = fullPath;
        }
    }
}
