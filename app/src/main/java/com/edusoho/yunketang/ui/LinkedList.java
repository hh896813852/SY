package com.edusoho.yunketang.ui;

public class LinkedList<E> {

    public LinkedList() {
    }

    Node<E> first;//头节点
    Node<E> last;//尾节点
    int size;//大小

    /**
     * 结点
     *
     * @param <E>
     */
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    /**
     * 在最后添加
     *
     * @param e
     */
    public void add(E e) {
        linkLast(e);
    }

    /**
     * 在最后添加
     *
     * @param e
     */
    private void linkLast(E e) {
        Node<E> newNode = new Node<>(last, e, null);
        Node<E> l = last;

        last = newNode;

        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }

    /**
     * 获取第index位置上的节点的值域
     *
     * @param index
     * @return
     */
    public E get(int index) {
        if (index < 0 || index > size) {
            return null;
        }
        return node(index).item;
    }

    /**
     * 获取第index位置上的节点
     *
     * @param index
     * @return
     */
    private Node<E> node(int index) {
        //index在前半部份 从前往后遍历
        if (index < (size >> 1)) {
            Node<E> node = first;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
            return node;
        } else { //index在后半部份 从后往前遍历
            Node<E> node = last;
            for (int i = size - 1; i > index; i--) {
                node = node.prev;
            }
            return node;
        }
    }

    /**
     * 在index的位置上添加一个元素
     *
     * @param index
     * @param e
     */
    public void add(int index, E e) {
        if (index < 0 || index > size) {
            return;
        }
        if (index == size) {
            linkLast(e);
        } else {
            Node<E> target = node(index);
            Node<E> pre = target.prev;
            Node<E> newNode = new Node<>(pre, e, target);
            //要考虑index=0时的情况
            if (pre == null) {
                first = newNode;
            } else {
                pre.next = newNode;
            }
            target.prev = newNode;
            size++;
        }
    }

    /**
     * @param index
     */
    public void remove(int index) {
        Node<E> target = node(index);
        unlinkNode(target);
    }

    private void unlinkNode(Node<E> p) {
        Node<E> pre = p.prev;
        Node<E> next = p.next;

        if (pre == null) {
            first = p.next;//删除头节点
        } else {
            pre.next = p.next;
        }

        if (next == null) {
            last = p.prev;//删除尾结节
        } else {
            next.prev = p.prev;
        }
        size--;
    }
}
