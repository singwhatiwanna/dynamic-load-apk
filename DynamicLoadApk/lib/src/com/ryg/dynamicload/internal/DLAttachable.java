/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 singwhatiwanna(任玉刚) <singwhatiwanna@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.ryg.dynamicload.internal;

import com.ryg.dynamicload.DLPlugin;

/**
 * @author mrsimple
 */
public interface DLAttachable {
    /**
     * when the proxy impl ( {@see DLProxyImpl#launchTargetActivity()} ) launch
     * the plugin activity , dl will call this method to attach the proxy activity
     * and pluginManager to the plugin activity. the proxy activity will load
     * the plugin's resource, so the proxy activity is a resource delegate for
     * plugin activity.
     * 
     * @param proxyActivity a instance of DLPlugin, {@see DLBasePluginActivity}
     *            and {@see DLBasePluginFragmentActivity}
     * @param pluginManager DLPluginManager instance, manager the plugins
     */
    public void attach(DLPlugin proxyActivity, DLPluginManager pluginManager);
}
