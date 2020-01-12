// @flow

// source: https://github.com/tommoor/react-emoji-render#array-output

import { toArray } from 'react-emoji-render'

function parseEmojis (text: string): string {
  const emojisArray = toArray(text)

  return emojisArray.reduce((previous, current) => {
    if (typeof current === 'string') {
      return previous + current
    }
    return previous + current.props.children
  }, '')
}

export default parseEmojis
