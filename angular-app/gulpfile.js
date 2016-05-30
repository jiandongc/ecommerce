var gulp = require('gulp'), gulpNgConfig = require('gulp-ng-config');

gulp.task('config', function () {
  gulp.src('config.json')
  .pipe(gulpNgConfig('config'))
  .pipe(gulp.dest('app/'));
});