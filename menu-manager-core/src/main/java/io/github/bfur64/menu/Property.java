package io.github.bfur64.menu;

import io.github.bfur64.menu.utils.AbstractProperty;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@NullMarked
public class Property<T> implements AbstractProperty<T> {
    private static final String NULL_PARSER_ERROR = "Parser is Null";
    private static final String INVALID_INPUT_ERROR = "Invalid Input";

    private final Supplier<T> getter;
    private final Consumer<T> setter;
    private final @Nullable Function<String, @Nullable T> parser;

    private final List<Predicate<T>> validators;
    private final List<String> errors;
    private @Nullable String latestError;

    public static <T> Builder<T> of(T initialValue) {
        Builder<T> builder = new Builder<>();
        return builder.create(initialValue);
    }

    private Property(Supplier<T> getter, Consumer<T> setter, List<Predicate<T>> validators, List<String> errors) {
        this(getter, setter, validators, errors, parser -> null);
    }

     private Property(Supplier<T> getter, Consumer<T> setter, List<Predicate<T>> validators, List<String> errors, @Nullable Function<String, @Nullable T> parser) {
        this.getter = getter;
        this.setter = setter;
        this.validators = validators;
        this.errors = errors;
        this.parser = parser;
    }

    @Override
    public T get() {
        return getter.get();
    }

    @Override
    public void set(T value) {
        setter.accept(value);
    }

    @Override
    public void setFromString(String value) {
        T parsed = parse(value);

        if (parsed == null) {
            throw new IllegalArgumentException(NULL_PARSER_ERROR);
        }

        setter.accept(parsed);
    }

    @Override
    public boolean isValid(T value) {
        for (int i = 0; i < validators.size(); i++) {
            if (!validators.get(i).test(value)) {
                latestError = errors.get(i);
                return false;
            }
        }

        latestError = null;
        return true;
    }

    @Override
    public boolean isValidFromString(String value) {
        T parsed = parse(value);

        if (parsed == null) {
            latestError = NULL_PARSER_ERROR;
            return false;
        }

        return isValid(parsed);
    }

    @Override
    public @Nullable String getLatestError() {
        return latestError;
    }

    @Override
    public @Nullable T parse(String value) {
        if (parser == null) return null;

        return parser.apply(value);
    }

    @NullUnmarked
    public static class Builder<T> {
        private Supplier<T> getter;
        private Consumer<T> setter;
        private @Nullable Function<String, @Nullable T> parser;

        private final List<Predicate<T>> validators = new ArrayList<>();
        private final List<String> errors = new ArrayList<>();

        private Builder<T> create(T initialValue) {
            ValueHolder<T> holder = new ValueHolder<>(initialValue);
            getter = () -> holder.value;
            setter = newValue -> holder.value = newValue;
            return this;
        }

        public Builder<T> require(Predicate<T> predicate) {
            validators.add(predicate);
            errors.add(INVALID_INPUT_ERROR);
            return this;
        }

        public Builder<T> require(Predicate<T> predicate, String error) {
            validators.add(predicate);
            errors.add(error);
            return this;
        }

        public Builder<T> setter(Consumer<T> setter) {
            this.setter = setter;
            return this;
        }

        public Builder<T> getter(Supplier<T> getter) {
            this.getter = getter;
            return this;
        }

        public Builder<T> parser(@Nullable Function<String, @Nullable T> parser) {
            this.parser = parser;
            return this;
        }

        public Property<@NonNull T> build() {
            return new Property<>(getter, setter, validators, errors, parser);
        }

        private static class ValueHolder<T> {
            T value;
            ValueHolder(T initialValue) { value = initialValue; }
        }
    }
}
