// @flow

const FLOAT_PRECISION = 4

export const renderFloat = (x: number) => Number(x.toPrecision(FLOAT_PRECISION)).toLocaleString()
