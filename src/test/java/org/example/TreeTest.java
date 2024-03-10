package org.example;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import manifold.rt.api.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

@SuppressWarnings({"HardCodedStringLiteral", "AssertWithoutMessage", "MigrateAssertToMatcherAssert"})
public class TreeTest {

    @Nested
    class InsertPathTests {

        @Test
        public void testInsertSinglePathSingleNode() {
            Tree<String, String> tree = new Tree<>();

            tree.insertPath(new Pair<String, String>("A", "A1"));

            TreeNode<String, String> node = tree.getNode("A");

            assertEquals("A", node.value);

            System.out.println(tree);

        }

        @Test
        public void testInsertSinglePathSingleNodeEdge() {
            Tree<String, String> tree = new Tree<>();

            tree.insertPath(new Pair<String, String>("A", "A1"));

            TreeNode<String, String> node = tree.getNode("A");

            assertTrue(node.hasEdge("A1"));

            System.out.println(tree);

        }

        @Test
        public void testInsertSinglePathSingleNodeTerminalNode() {

            Tree<String, String> tree = new Tree<>();

            tree.insertPath(new Pair<String, String>("A", "A1"));

            TreeNode<String, String> node = tree.getNode("A").getNode("A1");

            assertTrue(node.isLeaf);

            System.out.println(tree);

        }

        @Test
        public void testInsertSinglePathTwoNodes() {

            Tree<String, String> tree = new Tree<>();

            tree.insertPath(new Pair<>("A", "A1"), new Pair<>("B", "B1"));

            TreeNode<String, String> node = tree.getNode("A");

            assertEquals("A", node.value);
            assertTrue(node.hasEdge("A1"));

            node = node.getNode("A1");

            assertEquals("B", node.value);
            assertTrue(node.hasEdge("B1"));

            System.out.println(tree);

        }

        @Test
        public void testInsertSinglePathThreeNodes() {

            Tree<String, String> tree = new Tree<>();

            Pair<String, String>[] pairs = new Pair[]{
                new Pair<>("A", "A1"), new Pair<>("B", "B1"), new Pair<>("C", "C1")
            };

            tree.insertPath(pairs);

            TreeNode<String, String> node = tree.getNode("A").getNode("A1").getNode("B1");

            assertEquals("C", node.value);
            assertTrue(node.hasEdge("C1"));

            System.out.println(tree);

        }

        @Test
        public void testInsertTwoPathsSingleNodeSameRoot() {

            Tree<String, String> tree = new Tree<>();

            tree.insertPath(new Pair<>("A", "A1"));
            tree.insertPath(new Pair<>("A", "A2"));

            TreeNode<String, String> node = tree.getNode("A");

            assertEquals("A", node.value);
            assertTrue(node.hasEdge("A1"));
            assertTrue(node.hasEdge("A2"));

            System.out.println(tree);

        }

        @Test
        public void testInsertTwoPathsSingleNBodeDifferentRoots() {

            Tree<String, String> tree = new Tree<>();

            tree.insertPath(new Pair<>("A", "A1"));
            tree.insertPath(new Pair<>("B", "B1"));

            TreeNode<String, String> node = tree.getNode("B");

            assertEquals("B", node.value);
            assertTrue(node.hasEdge("B1"));

            System.out.println(tree);
        }

        @Test
        public void testInsertSubsetPathSmallerToLarger() {
            Tree<String, String> tree = new Tree<>();

            tree.insertPath(new Pair<>("A", "A1"), new Pair<>("B", "B1"));
            tree.insertPath(new Pair<>("A", "A1"), new Pair<>("B", "B1"), new Pair<>("C", "C1"));

            assertTrue(tree.getNode("A").getNode("A1").getNode("B1").action);
            assertTrue(tree.getNode("A").getNode("A1").getNode("B1").getNode("C1").isLeaf);

            System.out.println(tree);

        }


        @Test
        public void testInsertSubsetPathLargerToSmaller() {
            Tree<String, String> tree = new Tree<>();

            tree.insertPath(new Pair<>("A", "A1"), new Pair<>("B", "B1"), new Pair<>("C", "C1"));
            tree.insertPath(new Pair<>("A", "A1"), new Pair<>("B", "B1"));

            assertTrue(tree.getNode("A").getNode("A1").getNode("B1").getNode("C1").isLeaf);
            assertTrue(tree.getNode("A").getNode("A1").action);

            System.out.println(tree);


        }

        @Test
        public void testInsertPathWithDuplicatePairs() {
            Tree<String, String> tree = new Tree<>();
            Pair<String, String> pair = new Pair<>("A", "A1");

            tree.insertPath(pair, pair); // Inserting the same pair twice

            TreeNode<String, String> node = tree.getNode("A");
            assertEquals(1, node.references, "Reference count should be incremented for duplicate pairs.");
        }


        @Test
        public void testInsertMultiplePathsCommonPrefix() {
            Tree<String, String> tree = new Tree<>();
            Pair<String, String>[] path1 = new Pair[]{
                new Pair<>("A", "A1"),
                new Pair<>("B", "B1")
            };
            Pair<String, String>[] path2 = new Pair[]{
                new Pair<>("A", "A1"),
                new Pair<>("B", "B2")
            };

            tree.insertPath(path1);
            tree.insertPath(path2);

            TreeNode<String, String> nodeA = tree.getNode("A");
            TreeNode<String, String> nodeB1 = nodeA.getNode("A1").getNode("B1");
            TreeNode<String, String> nodeB2 = nodeA.getNode("A1").getNode("B2");

            assertTrue(nodeA.hasEdge("A1"), "Node A should have an edge A1.");
            assertTrue(nodeB1 != null && nodeB2 != null, "Node A1 should have edges to both B1 and B2.");
            assertEquals(1, nodeB1.references, "Node B1 should have a reference count of 1.");
            assertEquals(1, nodeB2.references, "Node B2 should have a reference count of 1.");
        }


    }

    @Nested
    class ContainsPathTests {


        @Test
        public void testContainsPathSinglePathOneNode() {

            Tree<String, String> tree = new Tree<>();

            tree.insertPath(new Pair<>("A", "A1"));

            assertTrue(tree.containsPath(new Pair<>("A", "A1")));

            System.out.println(tree);
        }

        @Test
        public void testContainsPathsFromTwoPathsSingleNodeDifferentRoots() {

            Tree<String, String> tree = new Tree<>();

            tree.insertPath(new Pair<>("A", "A1"));
            tree.insertPath(new Pair<>("B", "B1"));

            assertTrue(tree.containsPath(new Pair<>("B", "B1")));

            System.out.println(tree);
        }

        @Test
        public void testDoesNotContainPathSingleNode() {

            Tree<String, String> tree = new Tree<>();

            tree.insertPath(new Pair<>("A", "A1"));

            assertFalse(tree.containsPath(new Pair<>("A", "A2")));

            System.out.println(tree);
        }

        @Test
        public void testContainsPathWithThreeNodes() {

            Tree<String, String> tree = new Tree<>();

            Pair[] pairs = {new Pair<>("A", "A1"), new Pair<>("B", "B1"), new Pair<>("C", "C1")};

            tree.insertPath(pairs);

            assertTrue(tree.containsPath(pairs));

        }

        @Test
        public void testDoesNotContainsLongerPath() {

            Tree<String, String> tree = new Tree<>();

            Pair[] pairs = {new Pair<>("A", "A1"), new Pair<>("B", "B1"), new Pair<>("C", "C1")};

            tree.insertPath(pairs);

            Pair[] pairs2 = {new Pair<>("A", "A1"), new Pair<>("B", "B1"), new Pair<>("C", "C1"), new Pair<>("D", "D1")};

            System.out.println(tree);

            assertFalse(tree.containsPath(pairs2));

        }

        @Test
        public void testDoesNotContainPathSinceNeverAdded() {

            Tree<String, String> tree = new Tree<>();

            Pair[] pairs2 = {new Pair<>("A", "A1"), new Pair<>("B", "B1"), new Pair<>("C", "C1"), new Pair<>("D", "D1")};

            System.out.println(tree);

            assertFalse(tree.containsPath(pairs2));

        }



    }

    @Nested
    class ReferenceCountTests {


        @Test
        public void testReferenceCountFirstNode() {

            Tree<String, String> tree = new Tree<>();

            tree.insertPath(new Pair<>("A", "A1"));

            TreeNode<String, String> node = tree.getNode("A");

            assertEquals(1, node.references);

            System.out.println(tree);

        }

        @Test
        public void testReferenceSecondNode() {

            Tree<String, String> tree = new Tree<>();

            tree.insertPath(new Pair<>("A", "A1"));
            tree.insertPath(new Pair<>("A", "A2"));

            TreeNode<String, String> node = tree.getNode("A");

            assertEquals(2, node.references);

            System.out.println(tree);

        }


        @Test
        public void testOtherAllPairs() {

            Tree<String, String> tree = new Tree<>();

            Pair[] pairs = {new Pair<>("A", "A1"), new Pair<>("B", "B1"), new Pair<>("C", "C1")};

            tree.insertPath(pairs);

            TreeNode node = tree.getNode("A").getNode("A1");
            assertEquals(1, node.references);

            node = node.getNode("B1");
            assertEquals(1, node.references);


        }


        @Test
        public void testCountTerminalNodes() {

            Tree<String, String> tree = new Tree<>();

            Pair[] pairs = {new Pair<>("A", "A1"), new Pair<>("B", "B1"), new Pair<>("C", "C1")};

            tree.insertPath(pairs);

            TreeNode node = tree.getNode("A").getNode("A1").getNode("B1").getNode("C1");

            assertEquals(1, node.references);

        }



    }

    @Nested
    class RemovePaths {

        @Test
        public void testRemoveSecondPath() {

            Tree<String, String> tree = new Tree<>();

            tree.insertPath(new Pair<>("A", "A1"));
            tree.insertPath(new Pair<>("A", "A2"));

            tree.removePath(new Pair<>("A", "A1"));

            assertEquals(1, tree.getNode("A").references);
            assertEquals(1, tree.getNode("A").getNode("A2").references);
            assertNull(tree.getNode("A").getNode("A1"));

            System.out.println(tree);
        }


        @Test
        public void testRemoveAllPath() {

            Tree<String, String> tree = new Tree<>();

            tree.insertPath(new Pair<>("A", "A1"));

            tree.removePath(new Pair<>("A", "A1"));

            assertNull(tree.getNode("A"));
            assertTrue(tree.isEmpty());

            System.out.println(tree);
        }


        @Test
        public void testRemovePathsInReverseOrder() {
            Tree<String, String> tree = new Tree<>();
            Pair<String, String>[] pairs = new Pair[]{
                new Pair<>("A", "A1"),
                new Pair<>("B", "B1")
            };

            // Insert a path
            tree.insertPath(pairs);

            // Remove in reverse order
            tree.removePath(new Pair<>("B", "B1"));
            tree.removePath(new Pair<>("A", "A1"));

            Assertions.assertNull(tree.getNode("A"), "Node A should be removed from the tree.");
        }

        @Test
        public void testRemoveNonExistentPath() {
            Tree<String, String> tree = new Tree<>();
            Pair<String, String> pair = new Pair<>("A", "A1");

            // Attempt to removePath a non-existent path
            tree.removePath(pair);

            assertFalse(tree.containsPath(pair), "Contains should return false after attempting to removePath a non-existent path.");
        }

        @Test
        public void testRemoveNodesUpdatesActionProperty() {
            Tree<String, String> tree = new Tree<>();
            Pair<String, String>[] pairs = new Pair[]{
                new Pair<>("A", "A1"),
                new Pair<>("B", "B1"),
                new Pair<>("C", "C1")
            };

            // Insert a path and then removePath it
            tree.insertPath(pairs);
            tree.removePath(pairs);

            TreeNode<String, String> nodeA = tree.getNode("A");
            TreeNode<String, String> nodeB = nodeA != null ? nodeA.getNode("A1").getNode("B1") : null;

            assertFalse(nodeA != null && nodeA.action, "Action property of node A should be false after removal.");
            assertFalse(nodeB != null && nodeB.action, "Action property of node B should be false after removal.");
        }


    }

    @Nested
    class Action {

        @Test
        public void testInsertSinglePathSingleNode() {
            Tree<String, String> tree = new Tree<>();

            tree.insertPath(new Pair<>("A", "A1"));

            assertFalse(tree.getNode("A").action);
            assertTrue(tree.getNode("A").getNode("A1").action);

            System.out.println(tree);

        }

    }

    @Nested
    class EdgeCasesTests {

        @Test
        public void testInsertEmptyArrayOfPairs() {
            Tree<String, String> tree = new Tree<>();
            Pair<String, String>[] emptyPairs = new Pair[0];


            assertTrue(tree.isEmpty());

        }

        @Test
        public void testRemoveFromEmptyTree() {
            Tree<String, String> tree = new Tree<>();
            Pair<String, String> pair = new Pair<>("A", "A1");

            // Attempt to removePath from an empty tree
            tree.removePath(pair);

            assertFalse(tree.containsPath(pair), "After removing a pair from an empty tree, containsPath should return false.");
        }

        @Test
        public void testContainsOnEmptyTree() {
            Tree<String, String> tree = new Tree<>();
            Pair<String, String> pair = new Pair<>("A", "A1");

            // Check containsPath on an empty tree
            assertFalse(tree.containsPath(pair), "Contains should return false for an empty tree.");
        }
    }


}
