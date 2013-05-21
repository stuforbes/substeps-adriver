package uk.co.stfo.adriver.substeps;

import static org.hamcrest.CoreMatchers.is;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

public class PatternTests {
    @Test
    public void testPattern() {

        // TODO - use reflection to get hold of the string from the method
        final Pattern p = Pattern.compile("FindByTagAndAttributes tag=\"?([^\"]*)\"? attributes=\\[(.*)\\]");

        final String input = "FindByTagAndAttributes tag=\"input\" attributes=[type=submit,value=\"Search\"]";

        final Matcher matcher1 = p.matcher(input);
        final Matcher matcher = p.matcher(input);
        Assert.assertTrue(matcher1.matches());

        final int groupCount = matcher.groupCount();

        Assert.assertThat(groupCount, is(2));

        if (matcher.find()) {
            Assert.assertThat(matcher.group(1), is("input"));
            Assert.assertThat(matcher.group(2), is("type=submit,value=\"Search\""));
        } else {
            Assert.fail("should have found");
        }
    }


    @Test
    public void replacementTest() {
        String src = "AssertTagElementContainsText tag=\"([^\"]*)\" text=\"([^\"]*)\"";

        final String[] replacements = new String[] { "tag", "text" };

        for (final String s : replacements) {
            src = src.replaceFirst("\\([^\\)]*\\)", "<" + s + ">");
        }

        // System.out.println(src);
        final String desired = "AssertTagElementContainsText tag=\"<tag>\" text=\"<text>\"";

        // System.out.println(desired);

        Assert.assertThat(src, is(desired));
    }

}
