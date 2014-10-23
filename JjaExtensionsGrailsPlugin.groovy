/*
    This plugin is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

    See LICENSE.txt
*/

/**
 * Grails plugin to include my Groovy extension module
 *
 * @author jja@sinequanon.net
 * @see https://github.com/jja/grails-jja-extensions-plugin
 * @see String
 */
class JjaExtensionsGrailsPlugin {
    // the plugin version
    def version = '1.1.0'
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = '2.3 > *'
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        'grails-app/views/error.gsp',
        'web-app/**',
    ]

    // TODO Fill in these fields
    def title = 'jja extensions plugin' // Headline display name of the plugin
    def author = 'John Allison'
    def authorEmail = 'jja@sinequanon.net'
    def description = '''\
Include and load some Groovy extension modules and common Grails taglibs.
'''

    // URL to the plugin's documentation
    def documentation = 'http://github.com/jja/grails-jja-extensions-plugin/blob/master/README.md'

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = 'LGPL3'

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: 'http://github.com/jja/grails-jja-extensions-plugin' ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)

      // kludge to get Groovy extension modules loaded in Grails
      // http://grails.1312388.n4.nabble.com/groovy-extension-module-and-grails-td4642249.html
      // http://jira.grails.org/browse/GRAILS-10652
      // http://stackoverflow.com/questions/19564902/applying-groovy-extensions-in-grails-produces-missingmethodexception-for-string
      Map<org.codehaus.groovy.reflection.CachedClass, List<MetaMethod>> map = [:]
      ClassLoader classLoader = Thread.currentThread().contextClassLoader
      try {
        Enumeration<URL> resources = classLoader.getResources(org.codehaus.groovy.runtime.m12n.ExtensionModuleScanner.MODULE_META_INF_FILE)
        for (URL url in resources) {
          if (url.path.contains('groovy-all')) {
            // already registered
            continue
          }
          Properties properties = new Properties()
          InputStream inStream
          try {
            inStream = url.openStream()
            properties.load(inStream)
            GroovySystem.metaClassRegistry.registerExtensionModuleFromProperties(properties,
                              classLoader, map)
          }
          catch (IOException e) {
            throw new GroovyRuntimeException('Unable to load module META-INF descriptor', e)
          } finally {
            inStream?.close()
          }
        }
      }  catch (IOException ignored) {}
      map.each { org.codehaus.groovy.reflection.CachedClass cls, List<MetaMethod> methods ->
        cls.addNewMopMethods(methods)
      }

    }

    def doWithApplicationContext = { ctx ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}
