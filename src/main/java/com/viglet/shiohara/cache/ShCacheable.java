/*
 * Copyright (C) 2016-2018 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.viglet.shiohara.cache;

public interface ShCacheable
{
/* By requiring all objects to determine their own expirations, the
algorithm is abstracted from the caching service, thereby providing maximum
flexibility since each object can adopt a different expiration strategy.
*/
public boolean isExpired();
/* This method will ensure that the caching service is not responsible for
uniquely identifying objects placed in the cache.
*/
public Object getIdentifier();
}