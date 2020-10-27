BAZEL='bazel --bazelrc=.gcp.bazelrc --output_base=/tmp/_bazel'
$BAZEL info
$BAZEL build --config=gcp //... jflex/jflex_bin_deploy.jar
$BAZEL test  --config=gcp //...

