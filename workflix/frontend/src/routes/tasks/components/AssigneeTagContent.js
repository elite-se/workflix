// @flow

import React from 'react'
import type { UserType } from '../../../modules/datatypes/User'

type PropsType = {
  assignee: UserType
}

class AssigneeTagContent extends React.Component<PropsType> {
  render () {
    return this.props.assignee.name
  }
}

export default AssigneeTagContent
