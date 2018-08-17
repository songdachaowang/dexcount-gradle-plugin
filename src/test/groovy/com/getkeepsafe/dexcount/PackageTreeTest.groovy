/*
 * Copyright (C) 2015 KeepSafe Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.getkeepsafe.dexcount

import spock.lang.Specification

class PackageTreeTest extends Specification {
    def "adding duplicates increments count"() {
        setup:
        def tree = new PackageTree()
        tree.addMethodRef("com.foo.Bar")

        when:
        tree.addMethodRef("com.foo.Bar")

        then:
        tree.getMethodCount() == 2
    }

    def "can print a package list with classes included"() {
        setup:
        def writer = new StringBuilder()
        def tree = new PackageTree()

        when:
        tree.addMethodRef("com.foo.Bar")
        tree.addMethodRef("com.foo.Bar")
        tree.addMethodRef("com.foo.Qux")
        tree.addMethodRef("com.alpha.Beta")

        tree.printPackageList(writer, new PrintOptions(includeClasses: true))

        then:
        writer.toString() == """4        com
1        com.alpha
1        com.alpha.Beta
3        com.foo
2        com.foo.Bar
1        com.foo.Qux
"""
    }

    def "can print a package list without classes"() {
        setup:
        def writer = new StringBuilder()
        def tree = new PackageTree()

        when:
        tree.addMethodRef("com.foo.Bar")
        tree.addMethodRef("com.foo.Bar")
        tree.addMethodRef("com.foo.Qux")
        tree.addMethodRef("com.alpha.Beta")

        tree.printPackageList(writer, new PrintOptions(includeClasses: false))

        then:
        writer.toString() == """4        com
1        com.alpha
3        com.foo
"""
    }

    def "can print a tree"() {
        setup:
        def sb = new StringBuilder()
        def tree = new PackageTree()

        when:
        tree.addMethodRef("com.foo.Bar")
        tree.addMethodRef("com.foo.Bar")
        tree.addMethodRef("com.foo.Qux")
        tree.addMethodRef("com.alpha.Beta")

        tree.printTree(sb, new PrintOptions(includeClasses: true))

        then:
        sb.toString() == """com (4 methods)
  alpha (1 method)
    Beta (1 method)
  foo (3 methods)
    Bar (2 methods)
    Qux (1 method)
"""
    }

    def "accepts autogenerated class names"() {
        setup:
        def sb = new StringBuilder()
        def tree = new PackageTree()

        when:
        tree.addMethodRef('com.foo.bar.$$Generated$Class$$')

        tree.printPackageList(sb, new PrintOptions(includeClasses: true))

        then:
        def trimmed = sb.toString().trim()
        def ix = trimmed.lastIndexOf(' ')
        trimmed.substring(ix + 1) == 'com.foo.bar.$$Generated$Class$$'
    }

    def "prints a header when options say to"() {
        setup:
        def tree = new PackageTree()
        def sb = new StringBuilder()
        def opts = new PrintOptions()
        opts.printHeader = true

        when:
        tree.printPackageList(sb, opts)

        then:
        def trimmed = sb.toString().trim()
        trimmed == "methods  package/class name"
    }

    def "header includes column for fields when field count is specified"() {
        setup:
        def tree = new PackageTree()
        def sb = new StringBuilder()
        def opts = new PrintOptions()
        opts.printHeader = true
        opts.includeFieldCount = true

        when:
        tree.printPackageList(sb, opts)

        then:
        def trimmed = sb.toString().trim()
        trimmed == "methods  fields   package/class name"
    }

    def "package list can include field counts"() {
        setup:
        def tree = new PackageTree()
        def sb = new StringBuilder()
        def opts = new PrintOptions()
        opts.printHeader = true
        opts.includeFieldCount = true
        opts.includeClasses = true

        when:
        tree.addMethodRef("x.y.Z")
        tree.addMethodRef("x.y.Z")
        tree.addMethodRef("x.y.Z")
        tree.addFieldRef("x.y.Z")
        tree.addFieldRef("x.y.Z")
        tree.addFieldRef("x.y.W")
        tree.printPackageList(sb, opts)

        then:
        def trimmed = sb.toString().trim()
        def expected = """
methods  fields   package/class name
3        3        x
3        3        x.y
0        1        x.y.W
3        2        x.y.Z""".trim()

        trimmed == expected
    }

    def "package list can be sorted by method count"() {
        setup:
        def tree = new PackageTree()
        def sb = new StringBuilder()
        def opts = new PrintOptions()
        opts.printHeader = true
        opts.includeFieldCount = true
        opts.includeClasses = true
        opts.orderByMethodCount = true

        when:
        tree.addMethodRef("x.y.Z")
        tree.addMethodRef("x.y.Z")
        tree.addMethodRef("x.y.Z")
        tree.addFieldRef("x.y.Z")
        tree.addFieldRef("x.y.Z")
        tree.addFieldRef("x.y.W")
        tree.printPackageList(sb, opts)

        then:
        def trimmed = sb.toString().trim()
        def expected = """
methods  fields   package/class name
3        3        x
3        3        x.y
3        2        x.y.Z
0        1        x.y.W
""".trim()

        trimmed == expected
    }

    def "package list can include total method count"() {
        setup:
        def tree = new PackageTree()
        def sb = new StringBuilder()
        def opts = new PrintOptions()
        opts.includeTotalMethodCount = true
        opts.includeClasses = false
        opts.printHeader = false

        when:
        tree.addMethodRef("com.foo.Bar")
        tree.addMethodRef("com.foo.Qux")
        tree.addMethodRef("com.alpha.Beta")
        tree.addMethodRef("org.whatever.Foo")
        tree.addMethodRef("org.foo.Whatever")

        tree.printPackageList(sb, opts)

        then:
        def trimmed = sb.toString().trim();
        def expected = """
Total methods: 5
3        com
1        com.alpha
2        com.foo
2        org
1        org.foo
1        org.whatever
""".trim()

        trimmed == expected
    }

    def "packages can be YAML-formatted"() {
        setup:
        def tree = new PackageTree()
        def sb = new StringBuilder()
        def opts = new PrintOptions()
        opts.includeTotalMethodCount = true

        when:
        tree.addMethodRef("com.foo.Bar")
        tree.addMethodRef("com.foo.Qux")
        tree.addMethodRef("com.alpha.Beta")
        tree.addMethodRef("org.whatever.Foo")
        tree.addMethodRef("org.foo.Whatever")

        tree.printYaml(sb, opts)

        then:
        def trimmed = sb.toString().trim()
        def expected = """
---
methods: 5
counts:
  - name: com
    methods: 3
    children:
      - name: alpha
        methods: 1
        children: []
      - name: foo
        methods: 2
        children: []
  - name: org
    methods: 2
    children:
      - name: foo
        methods: 1
        children: []
      - name: whatever
        methods: 1
        children: []""".trim()

        trimmed == expected
    }
}
