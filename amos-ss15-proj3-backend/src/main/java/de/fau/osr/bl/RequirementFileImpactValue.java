/*
 * This file is part of ReqTracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * ReqTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ReqTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with ReqTracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.bl;
/**
 * This class represents a file requirement relationship based on impact
 * @author Gayathery Sathya
 */
public class RequirementFileImpactValue {

    float impactPercentage = -1;

    /**
     * constructor
     * @param impactPercentage value of percentage of impact
     */
    public RequirementFileImpactValue(float impactPercentage){

        this.impactPercentage = impactPercentage;
    }

    /**
     * @return returns the impact value as percentage
     */
    public float getImpactPercentage(){
        return impactPercentage;
    }

}
