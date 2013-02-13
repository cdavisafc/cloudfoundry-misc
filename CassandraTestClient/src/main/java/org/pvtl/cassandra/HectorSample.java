package org.pvtl.cassandra;

import me.prettyprint.cassandra.model.BasicColumnDefinition;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;

import java.awt.image.SampleModel;
import java.util.Collections;

import java.util.Iterator;
import java.util.List;

public class HectorSample {
    final String keySpaceName = "SampleKeyspace";
    final String columnFamilyName = "SampleColumnFamily";
    String cassandraNodeConnectionString = null; //"10.13.219.151:9160";

    private Cluster cluster = null;
    private Keyspace keyspace;
    private KeyspaceDefinition kDef;
    private long rowId = System.currentTimeMillis();

    public HectorSample(String connectionString) {
    	
    	cassandraNodeConnectionString = connectionString;
    	if (cluster == null) {
    		init();
    		createColumnFamily(columnFamilyName);
    	}

    }

    public String getConnectionString  () {
    	return cassandraNodeConnectionString;
    }

    public String getKeyspaceName () {
    	return keySpaceName;
    }
    
    public String getColumnFamilyName () {
    	return columnFamilyName;
    }


    public void init() {
        cluster = HFactory.getOrCreateCluster("Test Cluster", cassandraNodeConnectionString);
        
        kDef = cluster.describeKeyspace(keySpaceName);
        if (kDef == null) {

	        final KeyspaceDefinition newKeyspace = HFactory.createKeyspaceDefinition(keySpaceName,
	                ThriftKsDef.DEF_STRATEGY_CLASS, 1, Collections.<ColumnFamilyDefinition>emptyList());
	        cluster.addKeyspace(newKeyspace, false);
	        kDef = newKeyspace;
        }

        keyspace = HFactory.createKeyspace(keySpaceName, cluster);
    }
    
    private boolean columnFamilyExists(String columnFamilyName) {
    	List<ColumnFamilyDefinition> cfDefs = kDef.getCfDefs();
    	ColumnFamilyDefinition cfDef;
    	
    	Iterator<ColumnFamilyDefinition> it = cfDefs.iterator();
    	while (it.hasNext()) {
    		cfDef = it.next();
    		System.out.println("ColumnFamilyName = " + cfDef.getName());
    		if (cfDef.getName().equalsIgnoreCase(columnFamilyName))
    			return true;
    	}

    	return false;
    }

    public void createColumnFamily(String columnFamilyName) {
    	
    	if (!columnFamilyExists(columnFamilyName)) {
    	
	        final ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition(keySpaceName,
	                columnFamilyName, ComparatorType.BYTESTYPE);
	        cfDef.setKeyValidationClass(ComparatorType.LONGTYPE.getClassName());
	        cfDef.setComparatorType(ComparatorType.UTF8TYPE);
	
	        cfDef.addColumnDefinition(new BasicColumnDefinition() {{
	            setName(StringSerializer.get().toByteBuffer("id"));
	            setValidationClass(ComparatorType.LONGTYPE.getClassName());
	        }});
	        cfDef.addColumnDefinition(new BasicColumnDefinition() {{
	            setName(StringSerializer.get().toByteBuffer("fname"));
	            setValidationClass(ComparatorType.UTF8TYPE.getClassName());
	        }});
	        cfDef.addColumnDefinition(new BasicColumnDefinition() {{
	            setName(StringSerializer.get().toByteBuffer("lname"));
	            setValidationClass(ComparatorType.UTF8TYPE.getClassName());
	        }});
	        cfDef.addColumnDefinition(new BasicColumnDefinition() {{
	            setName(StringSerializer.get().toByteBuffer("city"));
	            setValidationClass(ComparatorType.UTF8TYPE.getClassName());
	        }});
	
	        cluster.addColumnFamily(cfDef);
    	}
    }

    public Long insertData(String columnFamilyName, String firstName, String lastName, String city) {
        final ColumnFamilyTemplate<Long, String> template = new ThriftColumnFamilyTemplate<Long, String>(keyspace,
                columnFamilyName, LongSerializer.get(), StringSerializer.get());
        final ColumnFamilyUpdater<Long, String> updater = template.createUpdater(++rowId);
        updater.setString("fname", firstName);
        updater.setString("lname", lastName);
        updater.setString("city", city);
        template.update(updater);

        return (rowId);
    }

    public Long get(String columnFamilyName, Long keyValue) {
        final ColumnFamilyTemplate<Long, String> template = new ThriftColumnFamilyTemplate<Long, String>(keyspace,
                columnFamilyName, LongSerializer.get(), StringSerializer.get());

        return template.queryColumns(keyValue).getKey();
    }
    public String getDetails(String columnFamilyName, Long keyValue) {
        final ColumnFamilyTemplate<Long, String> template = new ThriftColumnFamilyTemplate<Long, String>(keyspace,
                columnFamilyName, LongSerializer.get(), StringSerializer.get());
        return "Row with key value " + keyValue + 
        		" has FristName: " + template.queryColumns(keyValue).getString("fname") +
        		" has LastName: " + template.queryColumns(keyValue).getString("lname") + 
        		" has City: " + template.queryColumns(keyValue).getString("city");

    }
}