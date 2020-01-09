// @flow

import React from 'react'
import type { UserType } from '../../../../../modules/datatypes/User'
import type { TaskAssignmentType, TaskType } from '../../../../../modules/datatypes/Task'
import { Button } from '@blueprintjs/core'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'
import ProcessApi from '../../../../../modules/api/ProcessApi'
import { toastifyError } from '../../../../../modules/common/toastifyError'

type PropsType = {|
  task: TaskType,
  onAssignmentsChanged: (TaskAssignmentType[]) => void,
  user: UserType
|}

class AssignMeButton extends React.Component<PropsType, { loading: boolean }> {
  state = { loading: false }

  onClick = () => {
    this.setState({ loading: true })
    const { task, user, onAssignmentsChanged } = this.props
    new ProcessApi().addAssignee(task.id, user.id)
      .then(json => {
        this.setState({ loading: false })
        onAssignmentsChanged([...task.assignments, {
          id: json.newId,
          assigneeId: user.id,
          createdAt: new Date(),
          doneAt: null,
          closed: false
        }])
      })
      .catch(toastifyError)
  }

  render () {
    return <Button intent={Intent.PRIMARY} fill loading={this.state.loading} onClick={this.onClick}
            text='Assign me' icon='add' style={{ margin: '5px 0' }}/>
  }
}

export default AssignMeButton
