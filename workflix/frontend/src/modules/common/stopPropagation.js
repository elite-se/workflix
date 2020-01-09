// @flow

export default function stopPropagation (handler: () => void): (SyntheticEvent<HTMLElement> => void) {
  return event => {
    handler()
    event.stopPropagation()
  }
}
