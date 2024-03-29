package softarch.portal.db;

import softarch.portal.data.RawData;
import softarch.portal.data.RegularData;
import softarch.portal.data.UserProfile;
import softarch.portal.db.webservice.RegularWebServiceDatabase;

import java.util.List;
import java.util.Date;
import java.util.Properties;

/**
 * This class implements a facade for all of the database layer's functionality.
 * @author Niels Joncheere
 */
public class DatabaseFacade {
	private UserDatabase userDb;
	private RegularDatabase regularDb;
	private RawDatabase	rawDb;
	
	private RegularDatabase rWebServicedb;

	/**
	 * Creates a new database facade.
	 * @throws DatabaseException 
	 */
	public DatabaseFacade(Properties properties) {
		String dbType = properties.getProperty("dbType");
		AbstractDatabaseFactory df = DatabaseFactoryProducer.getFactory(dbType);
		
		userDb    = df.getUserDatabase(properties);
		regularDb = df.getRegularDatabase(properties);
		rawDb     = df.getRawDatabase(properties);
		
		rWebServicedb = new RegularWebServiceDatabase(properties);
	}

	/**
	 * Inserts a new user profile into the user database.
	 */
	public void insert(UserProfile profile)
		throws DatabaseException {
	
		userDb.insert(profile);
	}

	/**
	 * Updates an existing user profile in the user database.
	 */
	public void update(UserProfile profile)
		throws DatabaseException {

		userDb.update(profile);
	}

	/**
	 * Returns the user with the specified username.
	 */
	public UserProfile findUser(String username)
		throws DatabaseException {

		return userDb.findUser(username);
	}

	/**
	 * Checks whether a user with the specified username exists.
	 */
	public boolean userExists(String username)
		throws DatabaseException {

		return userDb.userExists(username);
	}

	/**
	 * Returns a list containing all records of the given information type
	 * that match the given query string.
	 */
	public List<RegularData> findRecords(String informationType, String queryString)
		throws DatabaseException {
		
		List<RegularData> localResults = regularDb
				.findRecords(informationType, queryString);
		
		List<RegularData> results = rWebServicedb
			.findRecords(informationType, queryString);
		
		results.addAll(localResults);
		return results;
	}

	/**
	 * Returns a list containing all records of the given information type
	 * that were added after the given date.
	 */
	public List<RegularData> findRecordsFrom(String informationType, Date date)
		throws DatabaseException {

		return regularDb.findRecordsFrom(informationType, date);
	}

	/**
	 * Adds a new regular data object to the regular database.
	 */
	public void add(RegularData rd)
		throws DatabaseException {
	
		regularDb.add(rd);
	}

	/**
	 * Returns the number of records of the given information type in the
	 * regular database.
	 */
	public int getNumberOfRegularRecords(String informationType)
		throws DatabaseException {

		return regularDb.getNumberOfRegularRecords(informationType);
	}

	/**
	 * Returns a list of all raw data.
	 */
	public List<RawData> getRawData()
		throws DatabaseException {

		return rawDb.getRawData();
	}

	/**
	 * Returns a specific raw data object.
	 */
	public RawData getRawData(int id)
		throws DatabaseException {

		return rawDb.getRawData(id);
	}

	public void addRawData(RegularData rd)
		throws DatabaseException {

		rawDb.addRawData(rd);
	}

	/**
	 * Deletes a raw data object.
	 */
	public void deleteRawData(RawData rd)
		throws DatabaseException {

		rawDb.deleteRawData(rd);
	}

	/**
	 * Updates a raw data object.
	 */
	public void updateRawData(RawData rd)
		throws DatabaseException {

		rawDb.updateRawData(rd);
	}

	/**
	 * Returns the number of records in the raw database.
	 */
	public int getNumberOfRawRecords()
		throws DatabaseException {

		return rawDb.getNumberOfRawRecords();
	}
}
