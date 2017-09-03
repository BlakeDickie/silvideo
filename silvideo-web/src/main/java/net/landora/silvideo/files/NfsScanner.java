/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.files;

import net.landora.silvideo.files.model.RootConnection;
import net.landora.silvideo.files.model.RootConnectionRepository;
import net.landora.silvideo.files.model.StorageFile;
import net.landora.silvideo.files.model.StorageFileRepository;
import net.landora.silvideo.files.model.StorageRoot;
import net.landora.silvideo.files.model.StorageRootRepository;
import net.landora.silvideo.files.vfs.VfsFile;
import net.landora.silvideo.files.vfs.VfsFolder;
import net.landora.silvideo.files.vfs.VfsItem;
import net.landora.silvideo.util.Ed2kDigest;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.List;
import javax.annotation.PostConstruct;

/**
 *
 * @author bdickie
 */
@Component
public class NfsScanner {

    @Autowired
    private RootConnectionRepository rootRepository;

    @Autowired
    private StorageRootRepository storageRepository;

    @Autowired
    private VfsFolderFactory vfsFolderFactory;

    @Autowired
    private StorageFileRepository fileRepository;

    @PostConstruct
    public void init() throws IOException {
//        StorageRoot root = new StorageRoot();
//        root.setRoots( new ArrayList<>( rootRepository.findAll() ) );
//        storageRepository.save( root );

        StorageRoot root = storageRepository.findAll().get( 0 );

        scanFolder( root );

//        storageRepository.save( root );
//        Nfs3 nfs = new Nfs3( "quon:/var/nfs-roots/storage/Videos", new CredentialUnix( 1000, 1000, Collections.EMPTY_SET ), 3 );
//        Nfs3File file = new Nfs3File( nfs, "/Muromi-san - 03.mkv" );
//        System.out.println( file.length() );
//        LocalRootConnection root = new LocalRootConnection();
//        root.setPath( "/var/storage/Video/Anime" );
//
//        NfsRootConnection root = new NfsRootConnection();
//        root.setNfsPath( "quon:/var/nfs-roots/storave/Videos" );
//        root.setGroupId( 1000 );
//        root.setUserId( 1000 );
//
//        rootRepository.save( root );
    }

    public void scanFolder( StorageRoot root ) {
        RootConnection rootConn = root.getRoots().get( 0 );
        VfsFolder vfs = vfsFolderFactory.toVfsFolder( rootConn );
        scan( vfs, root, "/" );
    }

    public void scan( VfsFolder vfs, StorageRoot root, String relPath ) {
        List<? extends VfsItem> children = vfs.getChildren();
        for ( VfsItem child : children ) {
            if ( child instanceof VfsFile ) {
                scan( (VfsFile) child, root, relPath + child.getName() );
            } else {
                scan( (VfsFolder) child, root, relPath + child.getName() + "/" );
            }

        }
    }

    public void scan( VfsFile vfs, StorageRoot root, String relPath ) {

        StorageFile file = fileRepository.findByRootAndPath( root, relPath );
        if ( file != null && file.getLastModified().equals( vfs.getLastModified() ) && file.getSize().equals( vfs.getSize() ) ) {
            return;
        }

        LoggerFactory.getLogger( getClass() ).warn( relPath );

        byte[] buffer = new byte[4096];

        try ( InputStream is = vfs.openInputStream() ) {
            long count = 0;
            int n;

            MessageDigest ed2k = new Ed2kDigest();
            MessageDigest md5 = MessageDigest.getInstance( "MD5" );
            MessageDigest sha512 = MessageDigest.getInstance( "SHA-512" );

            while ( ( n = is.read( buffer ) ) != IOUtils.EOF ) {
                ed2k.update( buffer, 0, n );
                md5.update( buffer, 0, n );
                sha512.update( buffer, 0, n );
                count += n;
            }

            if ( file == null ) {
                file = new StorageFile();
            }
            file.setFilename( vfs.getName() );
            file.setLastModified( vfs.getLastModified() );
            file.setPath( relPath );
            file.setSize( count );
            file.setRoot( root );

            file.setEd2kHash( Hex.encodeHexString( ed2k.digest() ) );
            file.setMd5Sum( Hex.encodeHexString( md5.digest() ) );
            file.setSha512Sum( Hex.encodeHexString( sha512.digest() ) );

            fileRepository.save( file );

        } catch ( Exception e ) {
            LoggerFactory.getLogger( getClass() ).error( "Error scanning file: " + relPath, e );
        }

    }

}
