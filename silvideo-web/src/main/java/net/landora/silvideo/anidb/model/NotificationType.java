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
package net.landora.silvideo.anidb.model;

public enum NotificationType {

    All(0),
    New(1),
    Group(2),
    Complete(3);

    private int type;
    private String name;

    private NotificationType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    private NotificationType(int type) {
        this.type = type;
        StringBuilder buffer = new StringBuilder();
        name = name();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (i > 0 && java.lang.Character.isUpperCase(c)) {
                buffer.append(" ");
            }
            buffer.append(c);
        }
        name = buffer.toString();
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public static NotificationType lookupType(int id) {
        return lookupType(id, 1);
    }

    public static NotificationType lookupType(int id, int pass) {

        for (NotificationType type : values()) {
            if (type.getType() == id) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getName();
    }

}
