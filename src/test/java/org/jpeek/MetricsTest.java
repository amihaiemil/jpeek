/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2018 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jpeek;

import com.jcabi.matchers.XhtmlMatchers;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import org.cactoos.collection.CollectionOf;
import org.cactoos.text.TextOf;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

// @todo #18:30min Impediment: #103 must be fixed before LCOM5 is tested
//  against: NoMethods, OneVoidMethodWithoutParams, WithoutAttributes,
//  OneMethodCreatesLambda. The LCOM5 value for these will be "NaN".
// @todo #18:30min Impediment: test for LCOM5 with "Bar" is not working
//  because the generated skeleton.xml is not including all attributes
//  being used by a method. See #114.
// @todo #93:30min NHD needs to be tested against the following after #103 is
//  fixed: NoMethods, OneVoidMethodWithoutParams, WithoutAttributes,
//  OneMethodCreatesLambda. NHD score for all these is "NaN".
// @todo #93:30min NHD calculation needs to take into account the method's
//  visibility, which should be configurable and implemented after #101 is
//  fixed.
// @todo #68:30min SCOM has an impediment on issue #103: cannot currently
//  be tested in MetricsTest when the resulting value is "NaN". Affected
//  tests are: NoMethods, OneVoidMethodWithoutParams, WithoutAttributes,
//  OneMethodCreatesLambda.
// @todo #68:30min SCOM has an impediment on issue #114: cannot currently
//  be tested against "Bar" because the skeleton is incorrectly excluding
//  some attributes from some methods that are using them.
// @todo #92:30min Impediment: test for LCOM2/3 with "Bar" is not working
//  because the generated skeleton.xml is not including all attributes
//  being used by a method. See #114.
// @todo #92:30min Impediment: test for LCOM3 with "OneMethodCreatesLambda"
//  does not work because the skeleton.xml creates a <method> for the
//  lambda with no way to discriminate it from regular methods.
/**
 * Tests for all metrics.
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.23
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle VisibilityModifierCheck (500 lines)
 * @checkstyle JavadocVariableCheck (500 lines)
 * @checkstyle MagicNumberCheck (500 lines)
 */
@RunWith(Parameterized.class)
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class MetricsTest {

    @Parameterized.Parameter(0)
    public String target;

    @Parameterized.Parameter(1)
    public String metric;

    @Parameterized.Parameter(2)
    public double value;

    @Parameterized.Parameters(name = "{0}:{1}:{2}")
    public static Collection<Object[]> targets() {
        return new CollectionOf<>(
            new Object[] {"Bar", "LCOM", 3.0d},
            new Object[] {"Foo", "LCOM", 0.0d},
            new Object[] {"MethodsWithDiffParamTypes", "LCOM", 9.0d},
            new Object[] {"NoMethods", "LCOM", 0.0d},
            new Object[] {"OneVoidMethodWithoutParams", "LCOM", 0.0d},
            new Object[] {"OverloadMethods", "LCOM", 0.0d},
            new Object[] {"TwoCommonAttributes", "LCOM", 3.0d},
            new Object[] {"WithoutAttributes", "LCOM", 0.0d},
            new Object[] {"OneMethodCreatesLambda", "LCOM", 1.0d},
            new Object[] {"Bar", "MMAC", 0.0d},
            new Object[] {"Foo", "MMAC", 1.0d},
            new Object[] {"MethodsWithDiffParamTypes", "MMAC", 0.0d},
            new Object[] {"NoMethods", "MMAC", 0.0d},
            new Object[] {"OneVoidMethodWithoutParams", "MMAC", 0.0d},
            new Object[] {"OverloadMethods", "MMAC", 0.3889d},
            new Object[] {"TwoCommonAttributes", "MMAC", 0.3333d},
            new Object[] {"WithoutAttributes", "MMAC", 1.0d},
            new Object[] {"OneMethodCreatesLambda", "MMAC", 0.0d},
            new Object[] {"Foo", "LCOM5", 0.0d},
            new Object[] {"MethodsWithDiffParamTypes", "LCOM5", 0.6d},
            new Object[] {"OverloadMethods", "LCOM5", 0.0d},
            new Object[] {"TwoCommonAttributes", "LCOM5", 1.0d},
            new Object[] {"Bar", "NHD", 0.3333d},
            new Object[] {"Foo", "NHD", 1.0d},
            new Object[] {"MethodsWithDiffParamTypes", "NHD", 0.6667d},
            new Object[] {"OverloadMethods", "NHD", 0.6111d},
            new Object[] {"TwoCommonAttributes", "NHD", 0.3333d},
            new Object[] {"MethodsWithDiffParamTypes", "CCM", 0.0667d},
            new Object[] {"Foo", "SCOM", 1.0d},
            new Object[] {"MethodsWithDiffParamTypes", "SCOM", 0.2d},
            new Object[] {"OverloadMethods", "SCOM", 1.0d},
            new Object[] {"TwoCommonAttributes", "SCOM", 0.0d},
            new Object[] {"Foo", "LCOM2", 0.0d},
            new Object[] {"MethodsWithDiffParamTypes", "LCOM2", 0.5d},
            new Object[] {"NoMethods", "LCOM2", 0.0d},
            new Object[] {"OneVoidMethodWithoutParams", "LCOM2", 0.0d},
            new Object[] {"OverloadMethods", "LCOM2", 0.0d},
            new Object[] {"TwoCommonAttributes", "LCOM2", 0.6667d},
            new Object[] {"WithoutAttributes", "LCOM2", 0.0d},
            new Object[] {"OneMethodCreatesLambda", "LCOM2", 1.0d},
            new Object[] {"Foo", "LCOM3", 0.0d},
            new Object[] {"MethodsWithDiffParamTypes", "LCOM3", 0.6d},
            new Object[] {"NoMethods", "LCOM3", 0.0d},
            new Object[] {"OneVoidMethodWithoutParams", "LCOM3", 0.0d},
            new Object[] {"OverloadMethods", "LCOM3", 0.0d},
            new Object[] {"TwoCommonAttributes", "LCOM3", 1.0d},
            new Object[] {"WithoutAttributes", "LCOM3", 0.0d}
        );
    }

    @Test
    public void testsTarget() throws IOException {
        final Path output = Files.createTempDirectory("");
        new Report(
            new Skeleton(new FakeBase(this.target)).xml(),
            this.metric
        ).save(output);
        MatcherAssert.assertThat(
            XhtmlMatchers.xhtml(
                new TextOf(
                    output.resolve(String.format("%s.xml", this.metric))
                ).asString()
            ),
            XhtmlMatchers.hasXPaths(
                String.format(
                    "//class[@id='%s' and number(@value)=%.4f]",
                    this.target, this.value
                )
            )
        );
    }

}
