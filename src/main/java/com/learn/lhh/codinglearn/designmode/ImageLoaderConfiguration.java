package com.learn.lhh.codinglearn.designmode;
/**
 * 使用生成器模式构建复杂的对象
 * 考虑这样的一个实际应用，Android图片异步加载框架，需要要创建图片加载配置的对象，里面很多属性的值都有约束，要求创建出来的对象是满足这些约束规则的。约束规则比如，线程池的数量不能小于2个、内存图片缓存的大小不能为负值等等。
 * 要想简洁直观、安全性好，有具有很好的扩展性地创建这个对象的话，一个较好的选择就是使用Builder模式，把复杂的创建过程通过Builder来实现。
 *
 * 采用Builder模式来构建复杂的对象，通常会对Builder模式进行一定的简化，因为目标明确，就是创建某个复杂对象，因此做适当简化会使程序更简洁。大致简化如下：
 *     由于是用Builder模式来创建某个对象，因此就没有必要再定义一个Builder接口，直接提供一个具体的构建器类就可以了。
 *     对于创建一个负责的对象，可能会有很多种不同的选择和步骤，干脆去掉“指导者”，把指导者的功能和Client的功能合并起来，也就是说，Client就相当于指导者，它来指导构建器类去构建需要的复杂对象。
 * ---------------------
 * 作者：Ricky_Fung
 * 来源：CSDN
 * 原文：https://blog.csdn.net/top_code/article/details/8469297
 * 版权声明：本文为博主原创文章，转载请附上博文链接！
 */

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
