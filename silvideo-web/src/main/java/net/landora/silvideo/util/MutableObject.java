/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.util;

import java.util.Objects;

/**
 *
 * @author bdickie
 */
public class MutableObject<T> {

    private T value;

    public MutableObject( T value ) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue( T value ) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Objects.toString( value );
    }

}
