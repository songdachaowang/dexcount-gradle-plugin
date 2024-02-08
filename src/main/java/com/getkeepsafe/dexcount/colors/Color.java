/*
 * Copyright (C) 2015-2021 KeepSafe Software
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
package com.getkeepsafe.dexcount.colors;

public enum Color {
    DEFAULT(Style.Normal),
    GREEN(Style.Identifier),
    YELLOW(Style.Info),
    RED(Style.Error);

    private final Style style;

    Color(Style style) {
        this.style = style;
    }

    Style getStyle() {
        return style;
    }
}
