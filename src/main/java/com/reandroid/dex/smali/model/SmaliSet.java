/*
 *  Copyright (C) 2022 github.com/REAndroid
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.reandroid.dex.smali.model;

import com.reandroid.dex.smali.SmaliReader;
import com.reandroid.dex.smali.SmaliWriter;
import com.reandroid.utils.collection.ArrayCollection;

import java.io.IOException;
import java.util.Iterator;
import java.util.function.Predicate;

public class SmaliSet<T extends Smali> extends Smali{

    private final ArrayCollection<T> body;

    public SmaliSet(){
        super();
        this.body = new ArrayCollection<>();
        this.body.setMonitor(new SmaliSetMonitor<>(this));
    }

    public Iterator<T> iterator() {
        return body.iterator();
    }
    public Iterator<T> iterator(int start) {
        return body.iterator(start);
    }
    public<T2> Iterator<T2> iterator(Class<T2> instance) {
        return body.iterator(instance);
    }
    public int size(){
        return body.size();
    }
    public boolean isEmpty(){
        return size() == 0;
    }

    public int indexOf(T smali) {
        return body.indexOf(smali);
    }
    public T get(int i) {
        return body.get(i);
    }
    public boolean add(T smali){
        return body.add(smali);
    }
    public boolean contains(T smali){
        return body.contains(smali);
    }
    public boolean remove(T smali){
        return body.remove(smali);
    }
    public T remove(int i) {
        return body.remove(i);
    }
    public boolean removeIf(Predicate<? super T> filter){
        return body.removeIf(filter);
    }
    public boolean removeInstances(Class<?> instance){
        return body.removeIf(instance::isInstance);
    }
    public void clear(){
        for (T smali : body) {
            smali.setParent(null);
        }
        body.clear();
    }
    void onRemoved(T item) {
        if(item != null) {
            item.setParent(null);
        }
    }
    void onAdded(T item) {
        if(item != null) {
            item.setParent(this);
        }
    }
    @Override
    public void append(SmaliWriter writer) throws IOException {
        writer.appendAllWithDoubleNewLine(iterator());
    }

    @Override
    public void parse(SmaliReader reader) throws IOException {
        while (parseNext(reader) != null) {
            reader.skipWhitespacesOrComment();
        }
    }
    public T parseNext(SmaliReader reader) throws IOException {
        if(reader.finished()) {
            return null;
        }
        T item = createNext(reader);
        if(item != null) {
            add(item);
            item.parse(reader);
        }
        return item;
    }
    T createNext(SmaliReader reader) {
        throw new RuntimeException("Method not implemented");
    }

    static class SmaliSetMonitor<T extends Smali> implements ArrayCollection.Monitor<T> {

        private final SmaliSet<T> smaliSet;

        SmaliSetMonitor(SmaliSet<T> smaliSet){
            this.smaliSet = smaliSet;
        }
        @Override
        public void onAdd(int i, T item) {
            smaliSet.onAdded(item);
        }
        @Override
        public void onRemoved(int i, T item) {
            smaliSet.onRemoved(item);
        }
    }
}
