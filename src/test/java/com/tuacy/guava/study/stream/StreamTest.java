package com.tuacy.guava.study.stream;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @name: StreamTest
 * @author: tuacy.
 * @date: 2019/7/16.
 * @version: 1.0
 * @Description:
 */
public class StreamTest {

    @Test
    public void collectionStream() {

        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
        // 使用List创建一个流对象
        Stream<Integer> stream = list.stream();
        // TODO: 对流对象做处理
    }

    @Test
    public void collectionParallelStream() {

        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
        // 使用List创建一个流对象
        Stream<Integer> stream = list.parallelStream();
        // TODO: 对流对象做处理
    }

    @Test
    public void arrayStream() {

        int[] intArray = new int[10];
        for (int index = 0; index < intArray.length; index++) {
            intArray[index] = index;
        }
        // 使用数组创建一个流对象
        IntStream stream = Arrays.stream(intArray);
        // TODO: 对流对象做处理
    }

    @Test
    public void bufferedReaderStream() {

        File file = new File("/home/tuacy/github/google-guava-study/src/main/resources/application.yml");
        try {
            // 把文件里面的内容一行一行的读出来
            BufferedReader in = new BufferedReader(new FileReader(file));
            // 生成一个Stream对象
            Stream<String> stream = in.lines();
            // TODO: 对流对象做处理
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fileWalkStream() {
        Path path = Paths.get("D:\\job\\git\\google-guava-study\\src\\main\\resources");
        try {
            // 第二个参数用于指定遍历几层
            Stream<Path> stream = Files.walk(path, 2);
            // TODO: 对流对象做处理
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fileLineStream() {
        Path path = Paths.get("D:\\job\\git\\google-guava-study\\src\\main\\resources\\application.yml");
        try {
            // 生成一个Stream对象
            Stream<String> stream = Files.lines(path);
            // TODO: 对流对象做处理
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fileFindStream() {
        Path path = Paths.get("D:\\job\\git\\google-guava-study\\src\\main\\resources");
        try {
            // 找到指定path下的所有不是目录的文件
            Stream<Path> stream = Files.find(path, 2, (path1, basicFileAttributes) -> {
                // 过滤掉目录文件
                return !basicFileAttributes.isDirectory();
            });
            // TODO: 对流对象做处理
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fileListStream() {
        Path path = Paths.get("D:\\job\\git\\google-guava-study\\src\\main\\resources");
        try {
            // 找到指定path下的所有的文件
            Stream<Path> stream = Files.list(path);
            // TODO: 对流对象做处理
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 归约
    @Test
    public void reduce() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        // 所有的元素相加，在加上20
        Integer reduceValue = stream.reduce(20, new BinaryOperator<Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                System.out.println(integer);
                System.out.println(integer2);
                return integer + integer2;
            }
        });
        System.out.println(reduceValue);
    }

    @Test
    public void collectList() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> intList = stream.collect(Collectors.toList());
    }

    @Test
    public void collectCounting() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Long count = stream.collect(Collectors.counting());
    }

    @Test
    public void collectMaxBy() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Optional<Integer> ret = stream.collect(Collectors.maxBy(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        }));
    }

    @Test
    public void collectMap() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//        List<Integer> intList = stream.collect(Collectors.toMap());
    }


    // 自己来组装Collector，返回一个List
    @Test
    public void collectNew() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> intList = stream.collect(new Collector<Integer, List<Integer>, List<Integer>>() {
            // 生成结果容器，容器类型为,我们这里为List<Integer>
            @Override
            public Supplier<List<Integer>> supplier() {
                return new Supplier<List<Integer>>() {

                    @Override
                    public List<Integer> get() {
                        return new ArrayList<>();
                    }
                };
            }

            // 把流里面的结果都放到结果容器里面去
            @Override
            public BiConsumer<List<Integer>, Integer> accumulator() {
                return new BiConsumer<List<Integer>, Integer>() {
                    @Override
                    public void accept(List<Integer> integers, Integer integer) {
                        integers.add(integer);
                    }
                };
            }

            // 两个两个合并并行执行的线程的执行结果，将其合并为一个最终结果A
            @Override
            public BinaryOperator<List<Integer>> combiner() {
                return new BinaryOperator<List<Integer>>() {
                    @Override
                    public List<Integer> apply(List<Integer> left, List<Integer> right) {
                        left.addAll(right);
                        return left;
                    }
                };
            }

            // 可以对最终的结果做一个转换操作
            @Override
            public Function<List<Integer>, List<Integer>> finisher() {
                return new Function<List<Integer>, List<Integer>>() {
                    @Override
                    public List<Integer> apply(List<Integer> integers) {
                        return integers;
                    }
                };
            }

            // 特征值
            @Override
            public Set<Characteristics> characteristics() {
                return EnumSet.of(Collector.Characteristics.UNORDERED, Collector.Characteristics.IDENTITY_FINISH);
            }
        });

        for (Integer item : intList) {
            System.out.println(item);
        }
    }

    @Test
    public void toCollection() {
        List<String> list = Arrays.asList("java", "ios", "c");
        LinkedList<String> retList = list.stream().collect(Collectors.toCollection(new Supplier<LinkedList<String>>() {

            @Override
            public LinkedList<String> get() {
                return new LinkedList<>();
            }
        }));
    }

    @Test
    public void toSet() {
        List<String> list = Arrays.asList("java", "ios", "c");
        Set<String> retList = list.stream().collect(Collectors.toSet());
    }

    @Test
    public void joining() {
        List<String> list = Arrays.asList("java", "ios", "c");
        String ret = list.stream().collect(Collectors.joining(":", "@@", "@@"));
        System.out.println(ret);//@@java:ios:c@@
    }

    @Test
    public void mapping() {
        List<String> list = Arrays.asList("java", "ios", "c");
        // 先把流里面的每个元素的前后加上[],之后在用:拼接起来
        String ret = list.stream().collect(Collectors.mapping(new Function<String, String>() {
            @Override
            public String apply(String s) {
                return "[" + s + "]";
            }
        }, Collectors.joining(":")));
        System.out.println(ret);//[java]:[ios]:[c]
    }

    @Test
    public void collectingAndThen() {
        List<String> list = Arrays.asList("java", "ios", "c");
        // Collectors.toList()之后在把List<String>通过Joiner转换String
        String ret = list.stream().collect(Collectors.collectingAndThen(Collectors.toList(), new Function<List<String>, String>() {
            @Override
            public String apply(List<String> strings) {
                return Joiner.on("; ")
                        .join(strings);
            }
        }));
        System.out.println(ret);//java; ios; c
    }


    @Test
    public void counting() {
        List<String> list = Arrays.asList("java", "ios", "c");
        // 元素个数
        Long ret = list.stream().collect(Collectors.counting());
        System.out.println(ret);//3
    }

    @Test
    public void minBy() {
        List<String> list = Arrays.asList("java", "ios", "c");
        // 这里简单的用字符串比较的大小
        Optional<String> ret = list.stream().collect(Collectors.minBy(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        }));
    }

    @Test
    public void summingInt() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        // 求和
        Integer ret = list.stream().collect(Collectors.summingInt(new ToIntFunction<Integer>() {
            @Override
            public int applyAsInt(Integer value) {
                return value;
            }

        }));
    }

    @Test
    public void averagingInt() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        // 求平均值
        Double ret = list.stream().collect(Collectors.averagingInt(new ToIntFunction<Integer>() {
            @Override
            public int applyAsInt(Integer value) {
                return value;
            }

        }));
    }

    @Test
    public void reducing() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        // 求平均值
        Integer ret = list.stream().collect(Collectors.reducing(10,
                new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) {
                        return integer * integer;
                    }
                },
                new BinaryOperator<Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) {
                        return integer + integer2;
                    }
                }));

        System.out.println(ret); // 10 + 1*1 + 2*2 + 3*3 = 24
    }

    @Test
    public void partitioningBy() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        // 求平均值
        Map<Boolean, List<Integer>> ret = list.stream()
                .collect(Collectors.partitioningBy(new Predicate<Integer>() {
                                                       @Override
                                                       public boolean test(Integer integer) {
                                                           return integer % 2 == 0;
                                                       }
                                                   },
                        Collectors.toList()));

        for (Map.Entry<Boolean, List<Integer>> entry : ret.entrySet()) {
            Boolean mapKey = entry.getKey();
            List<Integer> mapValue = entry.getValue();
            System.out.println(mapKey + ":" + mapValue);
        }
    }

    @Test
    public void collectGroupingBy() {
        List<Student> list = Arrays.asList(new Student("吴六", 26), new Student("张三", 26), new Student("李四", 27));
        Map<Integer, List<Student>> ret = list.stream().collect(Collectors.groupingBy(new Function<Student, Integer>() {
                                                                                          @Override
                                                                                          public Integer apply(Student student) {
                                                                                              return student.getAge();
                                                                                          }
                                                                                      },
                new Supplier<Map<Integer, List<Student>>>() {
                    @Override
                    public Map<Integer, List<Student>> get() {
                        return new HashMap<>();
                    }
                },
                Collectors.toList()));

        for (Map.Entry<Integer, List<Student>> entry : ret.entrySet()) {
            Integer mapKey = entry.getKey();
            List<Student> mapValue = entry.getValue();
            System.out.println(mapKey + ":" + mapValue);
        }

    }


    private static class Student {
        private String name;
        private int age;

        public Student(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

}
