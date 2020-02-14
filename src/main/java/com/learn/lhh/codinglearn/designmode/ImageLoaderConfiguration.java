package com.learn.lhh.codinglearn.designmodel;

import java.util.concurrent.Executor;

public final class ImageLoaderConfiguration {

    final Executor taskExecutor;

    final int memoryCacheSize;

    final int threadPoolSize;
    final int threadPriority;

    final boolean writeLogs;

    private ImageLoaderConfiguration(final Builder builder) {
        taskExecutor = builder.taskExecutor;
        threadPoolSize = builder.threadPoolSize;
        threadPriority = builder.threadPriority;
        memoryCacheSize = builder.memoryCacheSize;
        writeLogs = builder.writeLogs;
    }

    /**
     * Builder for {@link ImageLoaderConfiguration}
     *
     * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
     */
    public static class Builder {

        public static final int DEFAULT_THREAD_POOL_SIZE = 3;

        public static final int DEFAULT_THREAD_PRIORITY = Thread.NORM_PRIORITY - 1;

        private int memoryCacheSize = 0;

        private Executor taskExecutor = null;

        private int threadPoolSize = DEFAULT_THREAD_POOL_SIZE;
        private int threadPriority = DEFAULT_THREAD_PRIORITY;

        private boolean writeLogs = false;

        public Builder() {
        }

        public Builder taskExecutor(Executor executor) {
            if (threadPoolSize != DEFAULT_THREAD_POOL_SIZE || threadPriority != DEFAULT_THREAD_PRIORITY) {

            }

            this.taskExecutor = executor;
            return this;
        }

        public Builder threadPoolSize(int threadPoolSize) {

            this.threadPoolSize = threadPoolSize;
            return this;
        }

        public Builder threadPriority(int threadPriority) {

            if (threadPriority < Thread.MIN_PRIORITY) {
                this.threadPriority = Thread.MIN_PRIORITY;
            } else {
                if (threadPriority > Thread.MAX_PRIORITY) {
                    this.threadPriority = Thread.MAX_PRIORITY;
                } else {
                    this.threadPriority = threadPriority;
                }
            }
            return this;
        }

        public Builder memoryCacheSize(int memoryCacheSize) {
            if (memoryCacheSize <= 0) throw new IllegalArgumentException("memoryCacheSize must be a positive number");

            this.memoryCacheSize = memoryCacheSize;
            return this;
        }

        public Builder writeDebugLogs() {
            this.writeLogs = true;
            return this;
        }

        /** Builds configured {@link ImageLoaderConfiguration} object */
        public ImageLoaderConfiguration build() {
            initEmptyFieldsWithDefaultValues();
            return new ImageLoaderConfiguration(this);
        }

        private void initEmptyFieldsWithDefaultValues() {
            if (taskExecutor == null) {

            }
        }
    }
}
