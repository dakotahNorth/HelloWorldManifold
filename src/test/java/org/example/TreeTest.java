package org.example;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import manifold.rt.api.util.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

@SuppressWarnings({"HardCodedStringLiteral", "AssertWithoutMessage", "MigrateAssertToMatcherAssert"})
public class TreeTest {

    @Nested
    class InsertPairTests {

        @Test
        public void testInsertSinglePairsSinglePair() {
            Tree<String, String> tree = new Tree<>();

            tree.insert(new Pair<>("A", "A1"));

            TreeNode<String, String> node = tree.getName("A");

            assertEquals("A", node.name);
            assertTrue(node.containsValue("A1"));

            System.out.println(tree);

        }

        @Test
        public void testTerminalNode() {

            Tree<String, String> tree = new Tree<>();

            tree.insert(new Pair<>("A", "A1"));

            TreeNode<String, String> node = tree.getName("A").getValue("A1");

            assertTrue(node.isLeaf);

            System.out.println(tree);

        }

        @Test
        public void testInsertSinglePairTwoPairs() {

            Tree<String, String> tree = new Tree<>();

            tree.insert(new Pair[]{new Pair<>("A", "A1"), new Pair<>("B", "B1")});

            TreeNode<String, String> node = tree.getName("A");

            assertEquals("A", node.name);
            assertTrue(node.containsValue("A1"));

            System.out.println(tree);

        }

        @Test
        public void testInsertSingleThreePairs() {

            Tree<String, String> tree = new Tree<>();

            Pair<String, String>[] pairs = new Pair[]{
                new Pair<>("A", "A1"), new Pair<>("B", "B1"), new Pair<>("C", "C1")
            };

            tree.insert(pairs);

            TreeNode<String, String> node = tree.getName("A").getValue("A1").getValue("B1");

            assertEquals("C", node.name);
            assertTrue(node.containsValue("C1"));

            System.out.println(tree);

        }

        @Test
        public void testInsertTwoPairsSinglePairSameTags() {

            Tree<String, String> tree = new Tree<>();

            tree.insert(new Pair<>("A", "A1"));
            tree.insert(new Pair<>("A", "A2"));

            TreeNode<String, String> node = tree.getName("A");

            assertEquals("A", node.name);
            assertTrue(node.containsValue("A1"));
            assertTrue(node.containsValue("A2"));

            System.out.println(tree);

        }

        @Test
        public void testInsertTwoPairsSinglePairDifferentTags() {

            Tree<String, String> tree = new Tree<>();

            tree.insert(new Pair<>("A", "A1"));
            tree.insert(new Pair<>("B", "B1"));

            TreeNode<String, String> node = tree.getName("B");

            assertEquals("B", node.name);
            assertTrue(node.containsValue("B1"));

            System.out.println(tree);
        }
    }

    @Nested
    class ContainsPairsTests {


        @Test
        public void testContainsPairWithOnePair() {

            Tree<String, String> tree = new Tree<>();

            tree.insert(new Pair<>("A", "A1"));

            assertTrue(tree.contains(new Pair<>("A", "A1")));

            System.out.println(tree);
        }

        @Test
        public void testContainsPairWithTwoPairs() {

            Tree<String, String> tree = new Tree<>();

            tree.insert(new Pair<>("A", "A1"));
            tree.insert(new Pair<>("B", "B1"));

            assertTrue(tree.contains(new Pair<>("B", "B1")));

            System.out.println(tree);
        }

        @Test
        public void testDoesNotContainPair() {

            Tree<String, String> tree = new Tree<>();

            tree.insert(new Pair<>("A", "A1"));

            assertFalse(tree.contains(new Pair<>("A", "A2")));

            System.out.println(tree);
        }

        @Test
        public void testContainsLongPair() {

            Tree<String, String> tree = new Tree<>();

            Pair[] pairs = {new Pair<>("A", "A1"), new Pair<>("B", "B1"), new Pair<>("C", "C1")};

            tree.insert(pairs);

            assertTrue(tree.contains(pairs));

        }

        @Test
        public void testDoesNotContainsLongerPair() {

            Tree<String, String> tree = new Tree<>();

            Pair[] pairs = {new Pair<>("A", "A1"), new Pair<>("B", "B1"), new Pair<>("C", "C1")};

            tree.insert(pairs);

            Pair[] pairs2 = {new Pair<>("A", "A1"), new Pair<>("B", "B1"), new Pair<>("C", "C1"), new Pair<>("D", "D1")};

            System.out.println(tree);

            assertFalse(tree.contains(pairs2));

        }

        @Test
        public void testDoesNotContainsSinceNeverAdded() {

            Tree<String, String> tree = new Tree<>();

            Pair[] pairs2 = {new Pair<>("A", "A1"), new Pair<>("B", "B1"), new Pair<>("C", "C1"), new Pair<>("D", "D1")};

            System.out.println(tree);

            assertFalse(tree.contains(pairs2));

        }
    }

    @Nested
    class ReferenceCountTests {


        @Test
        public void testReferenceCountFirstPair() {

            Tree<String, String> tree = new Tree<>();

            tree.insert(new Pair<>("A", "A1"));

            TreeNode<String, String> node = tree.getName("A");

            assertEquals(1, node.references);

            System.out.println(tree);

        }

        @Test
        public void testReferenceSecondPair() {

            Tree<String, String> tree = new Tree<>();

            tree.insert(new Pair<>("A", "A1"));
            tree.insert(new Pair<>("A", "A2"));

            TreeNode<String, String> node = tree.getName("A");

            assertEquals(2, node.references);

            System.out.println(tree);

        }


        @Test
        public void testOtherAllPairs() {

            Tree<String, String> tree = new Tree<>();

            Pair[] pairs = {new Pair<>("A", "A1"), new Pair<>("B", "B1"), new Pair<>("C", "C1")};

            tree.insert(pairs);

            TreeNode node = tree.getName("A").getValue("A1");
            assertEquals(1, node.references);

            node = node.getValue("B1");
            assertEquals(1, node.references);


        }


        @Test
        public void testCountTerminalNodes() {

            Tree<String, String> tree = new Tree<>();

            Pair[] pairs = {new Pair<>("A", "A1"), new Pair<>("B", "B1"), new Pair<>("C", "C1")};

            tree.insert(pairs);

            TreeNode node = tree.getName("A").getValue("A1").getValue("B1").getValue("C1");

            assertEquals(1, node.references);

        }

    }

    @Nested
    class RemovePairs {

        @Test
        public void testRemoveSecondPair() {

            Tree<String, String> tree = new Tree<>();

            tree.insert(new Pair<>("A", "A1"));
            tree.insert(new Pair<>("A", "A2"));

            tree.remove(new Pair<>("A", "A1"));

            assertEquals(1, tree.getName("A").references);
            assertEquals(1, tree.getName("A").getValue("A2").references);
            assertNull(tree.getName("A").getValue("A1"));

            System.out.println(tree);
        }


        @Test
        public void testRemoveAllPairs() {

            Tree<String, String> tree = new Tree<>();

            tree.insert(new Pair<>("A", "A1"));

            tree.remove(new Pair<>("A", "A1"));

            assertNull(tree.getName("A"));

            System.out.println(tree);
        }

    }

    @Nested
    class Action {

        @Test
        public void testInsertSinglePairsSinglePair() {
            Tree<String, String> tree = new Tree<>();

            tree.insert(new Pair<>("A", "A1"));

            assertFalse(tree.getName("A").action);
            assertTrue(tree.getName("A").getValue("A1").action);

            System.out.println(tree);

        }

        @Test
        public void testSubsetPairsAction() {
            Tree<String, String> tree = new Tree<>();

            tree.insert(new Pair<>("A", "A1"), new Pair<>("B", "B1"), new Pair<>("C", "C1"));
            tree.insert(new Pair<>("A", "A1"), new Pair<>("B", "B1"));

            assertTrue(tree.getName("A").getValue("A1").getValue("B1").action);

            System.out.println(tree);

        }


    }



}