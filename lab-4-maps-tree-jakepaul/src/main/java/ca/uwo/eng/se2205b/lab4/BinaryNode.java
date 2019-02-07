package ca.uwo.eng.se2205b.lab4;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * Defines a Binary Node
 */
@ParametersAreNonnullByDefault
public abstract class BinaryNode<E, N extends BinaryNode<E, N>> implements Tree.Node<E> {
    
    abstract @Nullable N getLeft();

    abstract N setLeft(@Nullable N left);

    abstract @Nullable N getRight();

    abstract N setRight(@Nullable N right);

    abstract @Nullable N getParent();

    abstract @Nullable N setParent(@Nullable N parent);

    @Override
    public @Nonnull List<? extends N> children() {
        return null;
    }


    @Override
    public int height() {
        return 0;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isBalanced() {
        return false;
    }

    @Override
    public boolean isProper() {
        return false;
    }

    @Override
    public boolean isInternal() {
        return false;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
