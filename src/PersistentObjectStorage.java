import java.io.*;



public class PersistentObjectStorage
{
	private final String storagePath;



	/**
	 * Before you use any other method of this library you have to set a storage path.
	 * The storage path is always a directory with <code>directoryName</code> which will be created in the home path of the current user.
	 * <code>directoryName</code> gets trimmed.
	 *
	 * @param directoryName The name of the directory inside the home directory of the current user where all files are created and read with this library.
	 *
	 * @throws NullPointerException If <code>directoryName</code> is null
	 * @throws IllegalArgumentException If <code>directoryName</code> is empty
	 **/
	public PersistentObjectStorage(String directoryName)
	{
		if(directoryName==null)
		{
			throw new NullPointerException();
		}

		String trimmedDirectoryName=directoryName.trim();

		if(trimmedDirectoryName.isEmpty())
		{
			throw new IllegalArgumentException("DirectoryName can not be blank");
		}

		storagePath=System.getProperty("user.home")+(System.getProperty("os.name").contains("Windows")?"\\"+trimmedDirectoryName:"/"+trimmedDirectoryName);
	}


	/**
	 * Get the current set storage path as a <code>String</code>.
	 *
	 * @return Current set storage path
	 **/
  	public String getStoragePath()
	{
		return storagePath;
	}


	/**
	 * Checks if the file <code>notFirstStart</code> exists, so you can check if the application was started once before.
	 *
	 * @return True if file <code>notFirstStart</code> doesn't exist in the storage path
	 **/
	public boolean isFirstStart()
	{
		return !(new File(storagePath, "notFirstStart").exists());
	}

	/**
	 * If called, an empty file <code>notFirstStart</code> is created in the storage path.
	 * From now on <code>isFirstStart</code> returns false.
	 *
	 * @throws IOException If an I/O error occurred
	 **/
	public void firstStartFinished() throws IOException
	{
		File file=new File(storagePath, "notFirstStart");

		if(!file.exists())
		{
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
	}

	/**
	 * Deletes the file <code>notFirstStart</code> in the storage path if it exists.
	 * From now on <code>isFirstStart</code> returns true.
	 *
	 * @return True if notFirstStart file existed and was successfully deleted, else false
	 **/
	public boolean resetFirstStart()
	{
		File file=new File(storagePath, "notFirstStart");

		if(file.exists())
		{
			return file.delete();
		}

		return false;
	}


	/**
	 * Reads an object of a given type from a <code>File</code>.
	 *
	 * @param <T> The type of the object that is read
	 * @param fileName The name of the file an object should be read from
	 *
	 * @return Object of a given type
	 *
	 * @throws NullPointerException If <code>fileName</code> is null
	 * @throws FileNotFoundException If the file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading.
	 * @throws IOException If an I/O error occurred
	 * @throws ClassNotFoundException Class of a serialized object cannot be found
	 * @throws InvalidClassException Something is wrong with a class used by serialization
	 * @throws StreamCorruptedException Control information in the stream is inconsistent
	 * @throws OptionalDataException Primitive data was found in the stream instead of objects
	 * @throws ClassCastException If wrong class for T is given
	 **/
	public <T extends Serializable> T read(String fileName) throws IOException, ClassNotFoundException
	{
		FileInputStream fileReader=new FileInputStream(new File(storagePath, fileName));
		ObjectInputStream objectReader=new ObjectInputStream(fileReader);

		T readObject=(T) objectReader.readObject(); //!!!

		objectReader.close();
		fileReader.close();

		return readObject;
	}

	/**
	 * Writes an object of a given type to a <code>File</code>.
	 *
	 * @param <T> The type of the object that is written
	 * @param object The object of a given type that should be written
	 * @param fileName The name of the <code>File</code> the object should be written to
	 *
	 * @throws NullPointerException If <code>fileName</code> is null
	 * @throws FileNotFoundException If the file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
	 * @throws InvalidClassException Something is wrong with a class used by serialization
	 * @throws IOException If an I/O error occurred
	 **/
	public <T extends Serializable> void write(T object, String fileName) throws IOException
	{
		File targetFile=new File(storagePath, fileName);
		targetFile.getParentFile().mkdirs();

		FileOutputStream fileWriter=new FileOutputStream(targetFile, false);
		ObjectOutputStream objectWriter=new ObjectOutputStream(fileWriter);

		objectWriter.writeObject(object);

		objectWriter.close();
		fileWriter.close();
	}


	/**
	 * Deletes the directory where all files created with this library are stored.
	 * From now on <code>isFirstStart</code> returns true.
	 **/
	public void resetAllData()
	{
		deleteDirectoryRecursivly(new File(storagePath));
	}

	private void deleteDirectoryRecursivly(File deletingFile)
	{
		File[] allContents = deletingFile.listFiles();
		if (allContents != null)
		{
			for (File file : allContents)
			{
				deleteDirectoryRecursivly(file);
			}
		}

		deletingFile.delete();
	}

	public static void main(String[] args){}
}
