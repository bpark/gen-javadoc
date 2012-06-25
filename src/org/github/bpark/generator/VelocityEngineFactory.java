/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * This file is part of gen-javadoc.
 *
 * Copyright (c) 2011 by individual contributors as indicated by the
 * @author tags. See the copyright.txt file in the distribution
 * for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.github.bpark.generator;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.log.SimpleLog4JLogSystem;

/**
 * Factory to create a velocity engine.
 *
 * @author Burt Parkers
 */
public final class VelocityEngineFactory {

    /** The engine. */
    private static VelocityEngine engine;


    /** Private constructor. */
    private VelocityEngineFactory() {
    }

    /**
     * Creates a velocity engine, or returns an existing instance.
     *
     * @return the velocity engine.
     */
    public static VelocityEngine getVelocityEngine() {
        if (engine == null) {
            ExtendedProperties prop = new ExtendedProperties();
            prop.addProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, SimpleLog4JLogSystem.class.getName());
            prop.addProperty("runtime.log.logsystem.log4j.category", "GenerateToString");
            engine = new VelocityEngine();
            engine.setExtendedProperties(prop);
            engine.init();
        }

        return engine;
    }
}
