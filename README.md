[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.akexorcist/recycler-background-support-view/badge.svg)](https://search.maven.org/artifact/com.akexorcist/recycler-background-support-view)
![Minimum SDK Version](https://img.shields.io/badge/minSdkVersion-18-brightgreen)
[![Workflow Status](https://github.com/akexorcist/RecyclerBackgroundSupportView/actions/workflows/android.yml/badge.svg)](https://github.com/akexorcist/RecyclerBackgroundSupportView/actions) 

# Android-RecyclerBackgroundSupportView
Add Image View behind Recycler View with scrollable support

## Download
**Gradle**
```
implementation 'com.akexorcist:recycler-background-support-view:1.0.0'
```

## Usage
```xml
<com.akexorcist.recyclerbackgroundsupportview.RecyclerBackgroundSupportView
     android:id="@+id/recyclerBackgroundSupportView"
     android:layout_width="match_parent"
     android:layout_height="match_parent"
     app:rbsv_adjustViewBounds="true"
     app:rbsv_cropToPadding="true"
     app:rbsv_gravity="bottom"
     app:rbsv_scaleType="fitEnd"
     app:rbsv_src="@drawable/background"
     app:rbsv_tint="@color/green" />
```

```kotlin
recyclerBackgroundSupportView.getRecyclerView().layoutManager = LinearLayoutManager(context)
recyclerBackgroundSupportView.getRecyclerView().adapter = MainAdapter()
recyclerBackgroundSupportView.getImageView().setImageResource(R.drawable.background)
```

## Licence
Copyright 2021 Akexorcist

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
