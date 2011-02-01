/*
 * Copyright 2010 Alibaba Group Holding Limited.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.alibaba.toolkit.util.collection;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * �����<code>java.util.Map</code>.
 * </p>
 *
 * <p>
 * ����ӵ��<code>java.util.Map</code>��������������, <code>ListMap</code>�е� ��(<code>Map.Entry</code>)�������.
 * Ҳ����˵, �������Լ�ֵ(key)������, Ҳ����������(index)������. ����,
 * </p>
 *
 * <p>
 * ͨ��key����:
 * </p>
 * <pre>
 * Object value1 = listMap.get("key1");
 * </pre>
 *
 * <p>
 * ͨ������index, ȡ��key��value:
 * </p>
 * <pre>
 * Object value2 = listMap.get(2);
 * Object key2   = listMap.getKey(2);
 * </pre>
 *
 * <p>
 * ͨ������index, ɾ��һ��, �����ر�ɾ������:
 * </p>
 * <pre>
 * Map.Entry removedEntry = listMap.remove(3);
 * </pre>
 *
 * <p>
 * ����, �����ṩ����������: <code>keyList()</code>, <code>valueList()</code>��<code>entryList()</code>,
 * ����ȡ��key, value��entry��<code>List</code>. �����<code>Map.keySet()</code>, <code>Map.values()</code>
 * �Լ�<code>Map.entrySet()</code>, ����ֻ�ṩ��ȡ�������key��entry��<code>Set</code>,
 * �Լ�ȡ��value��<code>Collection</code>�ķ���.
 * </p>
 *
 * @version $Id: ListMap.java,v 1.1 2003/07/03 07:26:16 baobao Exp $
 * @author Michael Zhou
 */
public interface ListMap extends Map {
    /**
     * ����ָ��index����value.  ���index������Χ, ������<code>IndexOutOfBoundsException</code>.
     *
     * @param index Ҫ���ص�value������ֵ.
     *
     * @return ָ��index����value����
     */
    Object get(int index);

    /**
     * ����ָ��index����key. ���index������Χ, �򷵻�<code>IndexOutOfBoundsException</code>.
     *
     * @param index Ҫ���ص�key������ֵ.
     *
     * @return ָ��index����key����
     */
    Object getKey(int index);

    /**
     * ɾ��ָ��index������. ���index������Χ, �򷵻�<code>IndexOutOfBoundsException</code>.
     *
     * @param index Ҫɾ�����������ֵ.
     *
     * @return ��ɾ����<code>Map.Entry</code>��.
     */
    Map.Entry remove(int index);

    /**
     * ��������key��<code>List</code>.
     *
     * @return ����key��<code>List</code>.
     */
    List keyList();

    /**
     * ��������value��<code>List</code>.
     *
     * @return ����value��<code>List</code>.
     */
    List valueList();

    /**
     * ��������entry��<code>List</code>.
     *
     * @return ����entry��<code>List</code>.
     */
    List entryList();
}