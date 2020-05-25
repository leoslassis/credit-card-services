package br.com.itau.creditcardtransactionservices;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories({"br.com.itau.creditcardtransactionservices.repository"})
public class LocalhostMongoConfiguration extends AbstractMongoConfiguration {

	@Override
	protected String getDatabaseName() {
		return "creditCardTransaction";
	}

	@Override
	public MongoClient mongoClient() {
		return new MongoClient();
	}
	
}
