package sqlancer.cockroachdb;

import java.sql.SQLException;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import sqlancer.MainOptions.DBMSConverter;
import sqlancer.TestOracle;
import sqlancer.cockroachdb.CockroachDBProvider.CockroachDBGlobalState;
import sqlancer.cockroachdb.oracle.CockroachDBNoRECTester;
import sqlancer.cockroachdb.oracle.CockroachDBNoTableTester;
import sqlancer.cockroachdb.oracle.CockroachDBQueryPartitioningAggregateTester;
import sqlancer.cockroachdb.oracle.CockroachDBQueryPartitioningDistinctTester;
import sqlancer.cockroachdb.oracle.CockroachDBQueryPartitioningExtendedWhereTester;
import sqlancer.cockroachdb.oracle.CockroachDBQueryPartitioningGroupByTester;
import sqlancer.cockroachdb.oracle.CockroachDBQueryPartitioningHavingTester;
import sqlancer.cockroachdb.oracle.CockroachDBQueryPartitioningJoinTester;
import sqlancer.cockroachdb.oracle.CockroachDBQueryPartitioningTester;
import sqlancer.cockroachdb.oracle.CockroachDBQueryPartitioningWhereTester;

@Parameters(separators = "=", commandDescription = "Test CockroachDB")
public class CockroachDBOptions {

    @Parameter(names = "--oracle", converter = DBMSConverter.class)
    public CockroachDBOracle oracle = CockroachDBOracle.NOREC;

    public enum CockroachDBOracle {
        NOREC {
            @Override
            public TestOracle create(CockroachDBGlobalState globalState) throws SQLException {
                return new CockroachDBNoRECTester(globalState);
            }
        },
        AGGREGATE {

            @Override
            public TestOracle create(CockroachDBGlobalState globalState) throws SQLException {
                return new CockroachDBQueryPartitioningAggregateTester(globalState);
            }

        },
        NOTABLE {
            @Override
            public TestOracle create(CockroachDBGlobalState globalState) throws SQLException {
                return new CockroachDBNoTableTester(globalState);
            }
        },
        GROUP_BY {
            @Override
            public TestOracle create(CockroachDBGlobalState globalState) throws SQLException {
                return new CockroachDBQueryPartitioningGroupByTester(globalState);
            }
        },
        HAVING {
            @Override
            public TestOracle create(CockroachDBGlobalState globalState) throws SQLException {
                return new CockroachDBQueryPartitioningHavingTester(globalState);
            }
        },
        WHERE {
            @Override
            public TestOracle create(CockroachDBGlobalState globalState) throws SQLException {
                return new CockroachDBQueryPartitioningWhereTester(globalState);
            }
        },
        DISTINCT {
            @Override
            public TestOracle create(CockroachDBGlobalState globalState) throws SQLException {
                return new CockroachDBQueryPartitioningDistinctTester(globalState);
            }
        },
        EXTENDED_WHERE {
            @Override
            public TestOracle create(CockroachDBGlobalState globalState) throws SQLException {
                return new CockroachDBQueryPartitioningExtendedWhereTester(globalState);
            }
        },
        JOIN {
            @Override
            public TestOracle create(CockroachDBGlobalState globalState) throws SQLException {
                return new CockroachDBQueryPartitioningJoinTester(globalState);
            }
        },
        QUERY_PARTITIONING {
            @Override
            public TestOracle create(CockroachDBGlobalState globalState) throws SQLException {
                return new CockroachDBQueryPartitioningTester(globalState);
            }
        };

        public abstract TestOracle create(CockroachDBGlobalState globalState) throws SQLException;

    }

    @Parameter(names = {
            "--test_hash_indexes" }, description = "Test the USING HASH WITH BUCKET_COUNT=n_buckets option in CREATE INDEX")
    public boolean testHashIndexes = true;

    @Parameter(names = { "--test_temp_tables" }, description = "Test TEMPORARY tables")
    public boolean testTempTables = true;

    @Parameter(names = { "--increased_vectorization",
            "Generate VECTORIZE=on with a higher probability (which found a number of bugs in the past)" })
    public boolean makeVectorizationMoreLikely = true;

}
