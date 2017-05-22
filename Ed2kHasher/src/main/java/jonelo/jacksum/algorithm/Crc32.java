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

import java.util.zip.CRC32;

public class Crc32 extends AbstractChecksum {

    private CRC32 crc32 = null;

    public Crc32() {
        crc32 = new CRC32();
    }

    public void reset() {
        crc32.reset();
        length = 0;
    }

    public void update(byte[] buffer, int offset, int len) {
        crc32.update(buffer, offset, len);
        length += len;
    }

    public void update(int b) {
        crc32.update(b);
        length++;
    }

    public long getValue() {
        return crc32.getValue();
    }

    public String getHexValue() {
        String s = hexformat(getValue(), 8);
        return (uppercase ? s.toUpperCase() : s);
    }
}
