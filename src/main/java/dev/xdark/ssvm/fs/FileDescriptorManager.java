package dev.xdark.ssvm.fs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.LinkOption;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Maps VM file descriptors to native descriptors.
 *
 * @author xDark
 */
public interface FileDescriptorManager {

	int READ = 0;
	int WRITE = 1;
	int APPEND = 2;

	/**
	 * Maps VM file descriptor to {@link InputStream}.
	 *
	 * @param handle
	 * 		File descriptor to map.
	 *
	 * @return mapped stream.
	 */
	InputStream getFdIn(long handle);

	/**
	 * Maps VM file descriptor to {@link OutputStream}.
	 *
	 * @param handle
	 * 		File descriptor to map.
	 *
	 * @return mapped stream.
	 */
	OutputStream getFdOut(long handle);

	/**
	 * Called when VM closes file descriptor.
	 *
	 * @param handle
	 * 		VM descriptor handle.
	 *
	 * @throws IOException
	 * 		If any I/O error occurs.
	 */
	void close(long handle) throws IOException;

	/**
	 * Creates new VM file descriptor handle.
	 *
	 * @return file descriptor handle.
	 */
	long newFD();

	/**
	 * Returns new VM file descriptor for standard stream.
	 *
	 * @param stream
	 * 		Standard stream id.
	 *
	 * @return file descriptor handle.
	 */
	long newFD(int stream);

	/**
	 * Returns whether standard stream was opened for appending.
	 *
	 * @param stream
	 * 		Standard stream id.
	 *
	 * @return {@code true} if standard stream was opened for appending,
	 * {@code false} otherwise.
	 */
	boolean isAppend(int stream);

	/**
	 * Canonicalizes file path.
	 *
	 * @param path
	 * 		Path to canonicalize.
	 *
	 * @return canonicalized path.
	 *
	 * @throws IOException
	 * 		If any I/O error occurs.
	 */
	String canonicalize(String path) throws IOException;

	/**
	 * Opens file with the specific mode.
	 *
	 * @param path
	 * 		Path to open
	 * @param mode
	 * 		Open mode.
	 *
	 * @return file handle.
	 *
	 * @throws IOException
	 * 		If any I/O error occurs.
	 * @see FileDescriptorManager#READ
	 * @see FileDescriptorManager#WRITE
	 * @see FileDescriptorManager#APPEND
	 */
	long open(String path, int mode) throws IOException;

	/**
	 * Returns attributes of a file.
	 *
	 * @param path
	 * 		Path to file.
	 * @param attrType
	 * 		Attributes class type.
	 * @param options
	 * 		Options indicating how symbolic links are handled.
	 *
	 * @return file attributes or {@code null},
	 * if path does not exist.
	 *
	 * @throws IOException
	 * 		If any I/O error occurs.
	 */
	<A extends BasicFileAttributes> A getAttributes(String path, Class<A> attrType, LinkOption... options) throws IOException;

	/**
	 * List the elements of the directory denoted
	 * by the given abstract pathname.
	 *
	 * @param path
	 * 		Path to list.
	 *
	 * @return list of the elements of the directory.
	 */
	String[] list(String path);
}
