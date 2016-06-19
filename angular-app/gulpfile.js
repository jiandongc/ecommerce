var gulp = require('gulp'), gulpNgConfig = require('gulp-ng-config');

gulp.task('configDev', function () {
  gulp.src('config.json')
  .pipe(gulpNgConfig('config', {environment: 'local'}))
  .pipe(gulp.dest('app/'));
});

gulp.task('configUat', function () {
  gulp.src('config.json')
  .pipe(gulpNgConfig('config', {environment: 'uat'}))
  .pipe(gulp.dest('app/'));
});