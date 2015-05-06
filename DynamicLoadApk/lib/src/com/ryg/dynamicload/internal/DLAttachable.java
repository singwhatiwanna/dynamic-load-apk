/*
 * Copyright (C) 2014 singwhatiwanna(任玉刚) <singwhatiwanna@gmail.com>
 *
 * collaborator:田啸,宋思宇,Mr.Simple
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

package com.ryg.dynamicload.internal;

/**
 * 这是一个通用的attach接口，所有的组件都使用该接口即可
 */
public interface DLAttachable<T> {
    /**
     * when the proxy impl ( {@see DLProxyImpl#launchTargetActivity()} ) launch
     * the plugin activity , dl will call this method to attach the proxy
     * activity and pluginManager to the plugin activity. the proxy activity
     * will load the plugin's resource, so the proxy activity is a resource
     * delegate for plugin activity.
     * 
     * @param proxyComponent a instance of DLPlugin, {@see DLBasePluginActivity}
     *            and {@see DLBasePluginFragmentActivity}, {@see
     *            DLBasePluginService}
     * @param pluginManager DLPluginManager instance, manager the plugins
     */
    public void attach(T proxyComponent, DLPluginPackage pluginManager);
}
