package com.viglet.shiohara.mongodb;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.Storage;
import de.flapdoodle.embed.mongo.distribution.Version;

@Configuration
public class ShMongoConfig {

	@Value("${spring.shiohara.data.mongodb.host}")
    private String MONGO_DB_URL;

    @Value("${spring.shiohara.data.mongodb.port}")
    private int MONGO_DB_PORT;

	MongodStarter starter = MongodStarter.getDefaultInstance();
	MongodExecutable mongodExecutable;

	@Bean
	public MongoDbFactory mongoDbFactory() throws UnknownHostException {
		MongoClient mongo = new MongoClient(MONGO_DB_URL, MONGO_DB_PORT);
		return new SimpleMongoDbFactory(mongo, "store");
	}

	@Bean
	public MongoTemplate mongoTemplate() throws UnknownHostException {
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
		return mongoTemplate;
	}

	@PostConstruct
	public void construct() throws UnknownHostException, IOException {
		File userDir = new File(System.getProperty("user.dir"));
		if (userDir.exists() && userDir.isDirectory()) {
			File documentDir = new File(
					userDir.getAbsolutePath().concat(File.separator + "store" + File.separator + "document"));
			if (!documentDir.exists()) {
				documentDir.mkdirs();
			}

			Storage replication = new Storage(documentDir.getAbsolutePath(), null, 0);

			IMongodConfig mongodConfig = new MongodConfigBuilder().replication(replication)
					.version(Version.Main.PRODUCTION).net(new Net(MONGO_DB_URL, MONGO_DB_PORT, true)).build();
			mongodExecutable = starter.prepare(mongodConfig);
			MongodProcess mongod = mongodExecutable.start();
		}
	}

	@PreDestroy
	public void destroy() {
		if (mongodExecutable != null) {
			mongodExecutable.stop();
		}
	}
}