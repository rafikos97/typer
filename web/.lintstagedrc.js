module.exports = {
    "*.{js,ts,tsx,scss,css,md}": ["prettier . --write"],
    "*.ts": ["eslint", () => "ng test"],
};
