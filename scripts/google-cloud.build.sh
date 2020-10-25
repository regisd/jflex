BAZEL='bazel --bazelrc=.gcp.bazelrc'
$BAZEL info
$BAZEL build --config=gcp //... jflex/jflex_bin_deploy.jar
$BAZEL test  --config=gcp //...
find /workspace/bazel-testlogs/
