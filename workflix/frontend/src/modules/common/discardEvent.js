// @flow

export default function discardEvent<T> (handler: () => void): (SyntheticEvent<T> => void) {
  return () => handler()
}
