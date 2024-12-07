package org.thermoweb.aoc.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Predicate;

public class TreeNode<T> implements Iterable<TreeNode<T>> {
    T data;
    TreeNode<T> parent;
    ArrayList<TreeNode<T>> children;

    public TreeNode(T data){
        this.data = data;
        this.children = new ArrayList<>();
    }

    @Override
    public Iterator<TreeNode<T>> iterator() {
        TreeNodeIter<T> iter = new TreeNodeIter<T>(this);
        return iter;
    }

    public boolean isLeaf(){
        return this.children.size() == 0;
    }
    public boolean isRoot(){
        return this.parent == null;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public ArrayList<TreeNode<T>> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<TreeNode<T>> children) {
        this.children = children;
    }

    @SafeVarargs
    public final void addChild(T ...childData){
        this.addChild((n)->true,childData);
    }

    @SafeVarargs
    final public void addChild(Predicate<TreeNode<T>> p,T ...childData){
        var iter = this.iterator();
        ArrayList<TreeNode<T>> leaves = new ArrayList<>();
        iter.forEachRemaining((node)->{
            if (p.test(node)){
                leaves.add(node);
            }
        });
        leaves.stream().forEach((node)->{
            for (T newChild: childData){
                TreeNode<T> child = new TreeNode<T>(newChild);
                child.parent = node;
                node.children.add(child);
            }
        });
    }


    @SafeVarargs
    final public void addChildToAllLeaves(T ...childData){
        Predicate<TreeNode<T>> p = n->{
            return n.isLeaf();
        };
        this.addChild(p, childData);
    }

    @SafeVarargs
    final public void addChildToAllLeaves(Predicate<TreeNode<T>> p,T ...childData){
        Predicate<TreeNode<T>> q = n->{
            return p.test(n) && n.isLeaf();
        };
        this.addChild(q,childData);
    }

    @Override
    public String toString() {
        if (this.parent !=null){
            return "%s -> %s".formatted(parent.toString(),this.data.toString());
        }
        return "(root) %s".formatted(this.data.toString());
    }
}
