/*
 * This file copied from Grails 2.3.x.
 *
 * repo: git@github.com:grails/grails-core.git
 * branch: 2.3.x
 * commit: 83a64ce599c4bf7c12ffea899f15e1123401f3dd
 *
 * file: grails-test-suite-web/src/test/groovy/org/codehaus/groovy/grails/web/taglib/RenderTagLibTests.groovy
 *
 * web: https://github.com/grails/grails-core/blob/2.3.x/grails-test-suite-web/src/test/groovy/org/codehaus/groovy/grails/web/taglib/RenderTagLibTests.groovy
 * web: https://github.com/grails/grails-core/blob/83a64ce599c4bf7c12ffea899f15e1123401f3dd/grails-test-suite-web/src/test/groovy/org/codehaus/groovy/grails/web/taglib/RenderTagLibTests.groovy
 *
 * File reduced to tests for the sortableColumn tag, with additional tests for
 * the new tag features, by John Allison <jja@sinequanon.net>,
 * Feb 2016.
 */

/*
 * Copyright 2004-2005 the original author or authors.
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

package net.sinequanon.grails

/*
import grails.util.GrailsUtil

import org.codehaus.groovy.grails.commons.UrlMappingsArtefactHandler
import org.codehaus.groovy.grails.support.MockStringResourceLoader
import org.codehaus.groovy.grails.web.pages.FastStringWriter
import org.codehaus.groovy.grails.web.pages.GroovyPageBinding
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.codehaus.groovy.grails.web.sitemesh.FactoryHolder
import org.codehaus.groovy.grails.web.sitemesh.GSPSitemeshPage
import org.codehaus.groovy.grails.web.sitemesh.GrailsLayoutDecoratorMapper
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import org.springframework.web.servlet.support.RequestContextUtils as RCU

import com.opensymphony.module.sitemesh.RequestConstants
import com.opensymphony.module.sitemesh.html.util.CharArray
import com.opensymphony.module.sitemesh.parser.HTMLPageParser
import com.opensymphony.module.sitemesh.parser.TokenizedHTMLPage
*/

import org.codehaus.groovy.grails.web.taglib.*

/**
 * Tests for the RenderTagLib.groovy file which contains tags for rendering.
 *
 * @author Marcel Overdijk
 */
class SortableTagLibTests extends AbstractGrailsTagTests {

    void testSortableColumnTag() {
        final StringWriter sw = new StringWriter()
        final PrintWriter pw = new PrintWriter(sw)

        withTag("sortableColumn", pw) { tag ->
            webRequest.controllerName = "book"
            // use sorted map to be able to predict the order in which tag attributes are generated
            def attrs = new TreeMap([property:"title", title:"Title"])
            tag.call(attrs)

        }
        checkTagOutput(sw.toString(), 'sortable', 'asc', 'Title')
    }

    void testSortableColumnTagWithTitleKey() {
        final StringWriter sw = new StringWriter()
        final PrintWriter pw = new PrintWriter(sw)

        // test message not resolved; title property will be used (when provided)

        // without (default) title property provided
        withTag("sortableColumn", pw) { tag ->
            webRequest.controllerName = "book"
            // use sorted map to be able to predict the order in which tag attributes are generated
            def attrs = new TreeMap([property:"title", titleKey:"book.title"])
            tag.call(attrs)

        }
        checkTagOutput(sw.toString(), 'sortable', 'asc', 'book.title')

        sw = new StringWriter()
        pw = new PrintWriter(sw)

        // with (default) title property provided
        withTag("sortableColumn", pw) { tag ->
            webRequest.controllerName = "book"
            // use sorted map to be able to predict the order in which tag attributes are generated
            def attrs = new TreeMap([property:"title", title:"Title", titleKey:"book.title"])
            tag.call(attrs)

        }
        checkTagOutput(sw.toString(), 'sortable', 'asc', 'Title')

        // test message resolved

        sw = new StringWriter()
        pw = new PrintWriter(sw)

        messageSource.addMessage("book.title", RCU.getLocale(request), "Book Title")

        withTag("sortableColumn", pw) { tag ->
            webRequest.controllerName = "book"
            // use sorted map to be able to predict the order in which tag attributes are generated
            def attrs = new TreeMap([property:"title", title:"Title", titleKey:"book.title"])
            tag.call(attrs)

        }
        checkTagOutput(sw.toString(), 'sortable', 'asc', 'Book Title')
    }

    void testSortableColumnTagWithAction() {
        final StringWriter sw = new StringWriter()
        final PrintWriter pw = new PrintWriter(sw)

        withTag("sortableColumn", pw) { tag ->
            webRequest.controllerName = "book"
            // use sorted map to be able to predict the order in which tag attributes are generated
            def attrs = new TreeMap([action:"list2", property:"title", title:"Title"])
            tag.call(attrs)

        }
        checkTagOutput(sw.toString(), 'sortable', 'asc', 'Title')
    }

    void testSortableColumnTagWithDefaultOrder() {
        final StringWriter sw = new StringWriter()
        final PrintWriter pw = new PrintWriter(sw)

        // default order: desc

        withTag("sortableColumn", pw) { tag ->
            webRequest.controllerName = "book"
            // use sorted map to be able to predict the order in which tag attributes are generated
            def attrs = new TreeMap([property:"title", defaultOrder:"desc", title:"Title"])
            tag.call(attrs)

        }
        checkTagOutput(sw.toString(), 'sortable', 'desc', 'Title')

        // default order: asc

        sw = new StringWriter()
        pw = new PrintWriter(sw)

        withTag("sortableColumn", pw) { tag ->
            webRequest.controllerName = "book"
            // use sorted map to be able to predict the order in which tag attributes are generated
            def attrs = new TreeMap([property:"title", defaultOrder:"asc", title:"Title"])
            tag.call(attrs)

        }
        checkTagOutput(sw.toString(), 'sortable', 'asc', 'Title')

        // invalid default order

        sw = new StringWriter()
        pw = new PrintWriter(sw)

        withTag("sortableColumn", pw) { tag ->
            webRequest.controllerName = "book"
            // use sorted map to be able to predict the order in which tag attributes are generated
            def attrs = new TreeMap([property:"title", defaultOrder:"invalid", title:"Title"])
            tag.call(attrs)

        }
        checkTagOutput(sw.toString(), 'sortable', 'asc', 'Title')
    }

    void testSortableColumnTagWithAdditionalAttributes() {
        final StringWriter sw = new StringWriter()
        final PrintWriter pw = new PrintWriter(sw)

        withTag("sortableColumn", pw) { tag ->
            webRequest.controllerName = "book"
            // use sorted map to be able to predict the order in which tag attributes are generated
            // adding the class property is a dirty hack to predict the order; it will be overridden in the tag anyway
            def attrs = new TreeMap([property:"title", title:"Title", class:"other", style:"width: 200px;"])
            tag.call(attrs)

        }
        checkTagOutput(sw.toString(), 'other sortable', 'asc', 'Title', ' style="width: 200px;"')
    }

    void testSortableColumnTagSorted() {
        final StringWriter sw = new StringWriter()
        final PrintWriter pw = new PrintWriter(sw)

        // column sorted asc

        withTag("sortableColumn", pw) { tag ->
            webRequest.controllerName = "book"
            // set request params
            webRequest.getParams().put("sort", "title")
            webRequest.getParams().put("order", "asc")
            // use sorted map to be able to predict the order in which tag attributes are generated
            def attrs = new TreeMap([property:"title", title:"Title"])
            tag.call(attrs)

        }
        checkTagOutput(sw.toString(), 'sortable sorted asc', 'desc', 'Title')

        // column sorted desc

        sw = new StringWriter()
        pw = new PrintWriter(sw)

        withTag("sortableColumn", pw) { tag ->
            webRequest.controllerName = "book"
            // set request params
            webRequest.getParams().put("sort", "title")
            webRequest.getParams().put("order", "desc")
            // use sorted map to be able to predict the order in which tag attributes are generated
            def attrs = new TreeMap([property:"title", title:"Title"])
            tag.call(attrs)

        }
        checkTagOutput(sw.toString(), 'sortable sorted desc', 'asc', 'Title')

        // other column sorted

        sw = new StringWriter()
        pw = new PrintWriter(sw)

        withTag("sortableColumn", pw) { tag ->
            webRequest.controllerName = "book"
            // set request params
            webRequest.getParams().put("sort", "price")
            webRequest.getParams().put("order", "desc")
            // use sorted map to be able to predict the order in which tag attributes are generated
            def attrs = new TreeMap([property:"title", title:"Title"])
            tag.call(attrs)

        }
        checkTagOutput(sw.toString(), 'sortable', 'asc', 'Title')

        // sort in params attribute

        sw = new StringWriter()
        pw = new PrintWriter(sw)

        withTag("sortableColumn", pw) { tag ->
            webRequest.controllerName = "book"
            // set request params
            webRequest.getParams().put("sort", "price")
            webRequest.getParams().put("order", "desc")
            // use sorted map to be able to predict the order in which tag attributes are generated
            def attrs = new TreeMap([property:"title", title:"Title", params:[sort:"id"]])
            tag.call(attrs)

        }
        checkTagOutput(sw.toString(), 'sortable', 'asc', 'Title')
    }

    /**
     * Checks that the given output matches what is expected from the
     * tag, based on the given parameters. It ensures that the order
     * of the query parameters in the generated anchor's 'href' attribute
     * is not significant. If the output does not match the expected
     * text, an assertion is thrown.
     * @param output The output to check (String).
     * @param expectedClassValue The expected contents of the 'class'
     * attribute in the tag's output (String).
     * @param expectedOrder The expected sort order generated by the
     * tag (either 'asc' or 'desc').
     * @param expectedContent The expected content of the generated
     * anchor tag (String).
     */
    void checkTagOutput(output, expectedClassValue, expectedOrder, expectedContent) {
        // Check the output of the tag. The query parameters are not
        // guaranteed to be in any particular order, so we extract
        // them with a regular expression.
        def p = ~"<th class=\"${expectedClassValue}\" ><a href=\"\\S+?(\\w+=\\w+)&amp;(\\w+=\\w+)\">${expectedContent}</a></th>"
        def m = p.matcher(output)

        // First step: check the output as a whole matches what we
        // expect.
        assertTrue "Output [$output] doesn't match expected pattern", m.matches()

        // Now make sure the expected query parameters are there,
        // regardless of their order.
        if (m.group(1) == 'sort=title') {
            assertEquals m.group(2), "order=${expectedOrder}"
        }
        else {
            assertEquals m.group(1), "order=${expectedOrder}"
            assertEquals m.group(2), 'sort=title'
        }
    }

    /**
     * Checks that the given output matches what is expected from the
     * tag, based on the given parameters. It ensures that the order
     * of the query parameters in the generated anchor's 'href' attribute
     * is not significant. If the output does not match the expected
     * text, an assertion is thrown.
     * @param output The output to check (String).
     * @param expectedClassValue The expected contents of the 'class'
     * attribute in the tag's output (String).
     * @param expectedOrder The expected sort order generated by the
     * tag (either 'asc' or 'desc').
     * @param expectedContent The expected content of the generated
     * anchor tag (String).
     * @param otherAttrs Any additional attributes that will be passed
     * through by the tag. This string takes the form of the literal
     * text that will appear in the generated HTML (e.g.
     * ' style="width: 100%"'). Note that the string should normally
     * begin with a space (' ').
     */
    void checkTagOutput(output, expectedClassValue, expectedOrder, expectedContent, otherAttrs) {
        // Check the output of the tag. The query parameters are not
        // guaranteed to be in any particular order, so we extract
        // them with a regular expression.
        def p = ~"<th class=\"${expectedClassValue}\"${otherAttrs} ><a href=\"\\S+?(\\w+=\\w+)&amp;(\\w+=\\w+)\">${expectedContent}</a></th>"
        def m = p.matcher(output)

        // First step: check the output as a whole matches what we
        // expect.
        assert m.matches()

        // Now make sure the expected query parameters are there,
        // regardless of their order.
        if (m.group(1) == 'sort=title') {
            assertEquals m.group(2), "order=${expectedOrder}"
        }
        else {
            assertEquals m.group(1), "order=${expectedOrder}"
            assertEquals m.group(2), 'sort=title'
        }
    }

}
