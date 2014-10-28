package com.ldbc.driver.workloads.ldbc.snb.interactive;


import com.ldbc.driver.Operation;
import com.ldbc.driver.generator.CsvEventStreamReaderBasicCharSeeker;
import org.neo4j.csv.reader.CharSeeker;
import org.neo4j.csv.reader.Extractors;
import org.neo4j.csv.reader.Mark;

import java.io.IOException;
import java.util.Iterator;

public class Query8EventStreamReader implements Iterator<Operation<?>> {
    private final Iterator<Object[]> csvRows;

    public Query8EventStreamReader(Iterator<Object[]> csvRows) {
        this.csvRows = csvRows;
    }

    @Override
    public boolean hasNext() {
        return csvRows.hasNext();
    }

    @Override
    public Operation<?> next() {
        Object[] rowAsObjects = csvRows.next();
        return new LdbcQuery8(
                (long) rowAsObjects[0],
                LdbcQuery8.DEFAULT_LIMIT
        );
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException(String.format("%s does not support remove()", getClass().getSimpleName()));
    }

    public static class Query8Decoder implements CsvEventStreamReaderBasicCharSeeker.EventDecoder<Object[]> {
        /*
        Person
        3298543733056
        */
        @Override
        public Object[] decodeEvent(CharSeeker charSeeker, Extractors extractors, int[] columnDelimiters, Mark mark) throws IOException {
            long personId;
            if (charSeeker.seek(mark, columnDelimiters)) {
                personId = charSeeker.extract(mark, extractors.long_()).longValue();
            } else {
                // if first column of next row contains nothing it means the file is finished
                return null;
            }

            return new Object[]{personId};
        }
    }
}
