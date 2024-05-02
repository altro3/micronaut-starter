/*
 * Copyright 2017-2023 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.micrometer;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.util.NameUtils;

/**
 * Marker interface for Micronaut Registry features.
 */
public interface MicrometerRegistryFeature extends Feature {
    String EXPORT_PREFIX = "micronaut.metrics.export";

    @Override
    default String getCategory() {
        return Category.METRICS;
    }

    @Override
    default String getTitle() {
        return NameUtils.getNaturalName(io.micronaut.core.naming.NameUtils.dehyphenate(getName()));
    }

    @Override
    default void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
        addConfiguration(generatorContext);
    }

    default void addDependencies(@NonNull GeneratorContext generatorContext) {
        generatorContext.addDependency(micrometerDependency());
    }

    void addConfiguration(@NonNull GeneratorContext generatorContext);

    @NonNull
    default Dependency.Builder micrometerDependency() {
        return MicronautDependencyUtils.micrometerRegistryDependency(getImplementationName())
                .compile();
    }

    @Override
    default String getName() {
        return "micrometer-" + getImplementationName();
    }

    String getImplementationName();
}