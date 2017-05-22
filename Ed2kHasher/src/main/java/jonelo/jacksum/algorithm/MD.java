/**
 * Copyright (C) 2012-2014 Blake Dickie
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jonelo.jacksum.algorithm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD extends AbstractChecksum {

    private MessageDigest md = null;

    public MD(String arg) throws NoSuchAlgorithmException {
        length = 0;
        md = MessageDigest.getInstance(arg);
    }

    public void reset() {
        md.reset();
        length = 0;
    }

    public void update(byte[] buffer, int offset, int len) {
        md.update(buffer, offset, len);
        length += len;
    }

    public void update(byte b) {
        md.update(b);
        length++;
    }

    public String getHexValue() {
        return format(md.digest(), uppercase);
    }
}
