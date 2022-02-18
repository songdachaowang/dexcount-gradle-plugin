/*
 * Copyright (C) 2015-2016 KeepSafe Software
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

data class PrintOptions(
    var includeClasses: Boolean = false,
    var includeClassCount: Boolean = false,
    var includeMethodCount: Boolean = true,
    var includeFieldCount: Boolean = false,
    var includeTotalMethodCount: Boolean = false,
    var teamCityIntegration: Boolean = false,
    var printHeader: Boolean = false,
    var orderByMethodCount: Boolean = false,
    var maxTreeDepth: Int = Integer.MAX_VALUE,
    var printDeclarations: Boolean = false,
    var isAndroidProject: Boolean = true
)
