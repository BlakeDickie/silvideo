/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.landora.silvideo.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

/**
 *
 * @author bdickie
 */
public class Ed2kDigest extends MessageDigest {

    private final static String AUX_ALGORITHM = "MD4";
    private MessageDigest md4 = null;
    private MessageDigest md4final = null;
    private final static int BLOCKSIZE = 9728000; // 9500 * 1024;
    private final byte[] edonkeyHash = new byte[16]; // 16 bytes, 128 bits

    protected long value;
    protected long length;

    static {
        if ( Security.getProviders( "MessageDigest.MD4" ) == null ) {
            Security.addProvider( new BouncyCastleProvider() );
        }
    }

    public Ed2kDigest() throws NoSuchAlgorithmException {
        super( "ed2k" );

        md4 = MessageDigest.getInstance( AUX_ALGORITHM );
        md4final = MessageDigest.getInstance( AUX_ALGORITHM );
    }

    @Override
    protected void engineUpdate( byte b ) {

        md4.update( b );
        length++;

        if ( ( length % BLOCKSIZE ) == 0 ) {
            System.arraycopy( md4.digest(), 0, edonkeyHash, 0, 16 );
            md4final.update( edonkeyHash, 0, 16 );

            md4.reset();
        }
    }

    @Override
    protected void engineUpdate( byte[] buffer, int offset, int len ) {
        int zuSchreiben = len - offset; // XXX
        int passed = (int) ( length % BLOCKSIZE );
        int platz = BLOCKSIZE - passed;

        // |___________XXX....|_____
        if ( platz > zuSchreiben ) {
            md4.update( buffer, offset, len );
            length += len;
        } else // |_______________XXX|_____
        if ( platz == zuSchreiben ) {
            md4.update( buffer, offset, len );
            length += len;
            System.arraycopy( md4.digest(), 0, edonkeyHash, 0, 16 );
            md4final.update( edonkeyHash, 0, 16 );

            md4.reset();
        } else // |________________XX|X____
        if ( platz < zuSchreiben ) {
            md4.update( buffer, offset, platz );
            length += platz;

            System.arraycopy( md4.digest(), 0, edonkeyHash, 0, 16 );
            md4final.update( edonkeyHash, 0, 16 );

            md4.reset();

            md4.update( buffer, offset + platz, zuSchreiben - platz );
            length += zuSchreiben - platz;
        }
    }

    @Override
    protected byte[] engineDigest() {
        if ( length < BLOCKSIZE ) // if only one block, partial md4 hash = final hash
        {
            System.arraycopy( md4.digest(), 0, edonkeyHash, 0, 16 );
        } else {
            // if more then one block, final hash = hash of all partial hashes
            md4final.update( md4.digest(), 0, 16 );
            System.arraycopy( md4final.digest(), 0, edonkeyHash, 0, 16 );
        }
        return edonkeyHash;
    }

    @Override
    protected void engineReset() {
        md4.reset();
        md4final.reset();
        length = 0;
    }

}
