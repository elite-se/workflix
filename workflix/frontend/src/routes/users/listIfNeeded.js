// @flow

import { isEmpty } from 'lodash'
import { UL } from '@blueprintjs/core'
import React from 'react'

export default function listIfNeeded <T> (values: T[], keySupply: T => number, renderer: T => React$Node): React$Node {
  if (isEmpty(values)) {
    return '–'
  } else if (values.length === 1) {
    return renderer(values[0])
  } else {
    // eslint-disable-next-line react/jsx-pascal-case
    return <UL style={{ listStylePosition: 'inside', padding: 0, margin: 0 }}>
      {values.map(value => <li key={keySupply(value)}>{renderer(value)}</li>)}
    </UL>
  }
}
