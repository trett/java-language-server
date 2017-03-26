package org.javacs;

import org.eclipse.lsp4j.*;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class GotoTest {
    private static final Logger LOG = Logger.getLogger("main");
    private static final String file = "/org/javacs/example/Goto.java";
    private static final URI uri = FindResource.uri(file);
    private static final String defaultConstructorFile = "/org/javacs/example/GotoDefaultConstructor.java";
    private static final URI defaultConstructorUri = FindResource.uri(defaultConstructorFile);

    @Test
    public void localVariable() throws IOException {
        List<? extends Location> suggestions = doGoto(file, 9, 8);

        assertThat(suggestions, contains(location(uri, 4, 8, 4, 21)));
    }

    @Test
    public void defaultConstructor() throws IOException {
        List<? extends Location> suggestions = doGoto(defaultConstructorFile, 4, 45);

        assertThat(suggestions, contains(location(defaultConstructorUri, 2, 13, 2, 35)));
    }

    @Test
    public void constructor() throws IOException {
        List<? extends Location> suggestions = doGoto(file, 10, 20);

        assertThat(suggestions, contains(location(uri, 43, 11, 43, 15)));
    }

    @Test
    public void className() throws IOException {
        List<? extends Location> suggestions = doGoto(file, 15, 8);

        assertThat(suggestions, contains(location(uri, 2, 13, 2, 17)));
    }

    @Test
    public void staticField() throws IOException {
        List<? extends Location> suggestions = doGoto(file, 12, 21);

        assertThat(suggestions, contains(location(uri, 35, 25, 35, 36)));
    }

    @Test
    public void field() throws IOException {
        List<? extends Location> suggestions = doGoto(file, 13, 21);

        assertThat(suggestions, contains(location(uri, 36, 18, 36, 23)));
    }

    @Test
    public void staticMethod() throws IOException {
        List<? extends Location> suggestions = doGoto(file, 15, 13);

        assertThat(suggestions, contains(location(uri, 37, 25, 37, 37)));
    }

    @Test
    public void method() throws IOException {
        List<? extends Location> suggestions = doGoto(file, 16, 13);

        assertThat(suggestions, contains(location(uri, 40, 18, 40, 24)));
    }

    @Test
    public void staticMethodReference() throws IOException {
        List<? extends Location> suggestions = doGoto(file, 18, 26);

        assertThat(suggestions, contains(location(uri, 37, 25, 37, 37)));
    }

    @Test
    public void methodReference() throws IOException {
        List<? extends Location> suggestions = doGoto(file, 19, 26);

        assertThat(suggestions, contains(location(uri, 40, 18, 40, 24)));
    }

    @Test
    @Ignore // TODO
    public void typeParam() throws IOException {
        List<? extends Location> suggestions = doGoto(file, 45, 11);

        assertThat(suggestions, contains(location(uri, 2, 18, 2, 23)));
    }

    private Location location(URI uri, int startRow, int startColumn, int endRow, int endColumn) {
        Position start = new Position();

        start.setLine(startRow);
        start.setCharacter(startColumn);

        Position end = new Position();

        end.setLine(startRow);
        end.setCharacter(endColumn);

        Range range = new Range();

        range.setStart(start);
        range.setEnd(end);

        Location location = new Location();

        location.setUri(uri.toString());
        location.setRange(range);

        return location;
    }

    private static final JavaLanguageServer server = LanguageServerFixture.getJavaLanguageServer();

    private List<? extends Location> doGoto(String file, int row, int column) throws IOException {
        TextDocumentIdentifier document = new TextDocumentIdentifier();

        document.setUri(FindResource.uri(file).toString());

        Position position = new Position();

        position.setLine(row);
        position.setCharacter(column);

        TextDocumentPositionParams p = new TextDocumentPositionParams();

        p.setTextDocument(document);
        p.setPosition(position);

        return server.gotoDefinition(p);
    }

}
