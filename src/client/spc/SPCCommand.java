package net.minecraft.src;

import java.lang.annotation.*;

/**
 * @author _303, simo_415 Copyright (C) 2010-2011 - (http://bit.ly/spcmod)
 * 
 *         This file is part of Single Player Commands.
 * 
 *         Single Player Commands is free software: you can redistribute it and/or modify it under
 *         the terms of the GNU Lesser General Public License as published by the Free Software
 *         Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 *         Single Player Commands is distributed in the hope that it will be useful, but WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *         PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 *         You should have received a copy of the GNU Lesser General Public License along with
 *         Single Player Commands. If not, see <http://www.gnu.org/licenses/>.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SPCCommand {

   String cmd();

   String help() default "";

   String args() default "";

   String example() default "";
   
   boolean multiplayer() default false;
   
   String[] alias() default {};
}
