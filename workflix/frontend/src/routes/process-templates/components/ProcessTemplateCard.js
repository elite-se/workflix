// @flow

import React from 'react'
import { Alert, Button, ButtonGroup, Card, Colors, H3, H4, Icon } from '@blueprintjs/core'
import type { ProcessTemplateType } from '../../../modules/datatypes/Process'
import { Link, navigate } from '@reach/router'
import type { StyledComponent } from 'styled-components'
import styled from 'styled-components'
import type { UserType } from '../../../modules/datatypes/User'
import { calcGraph } from '../../../modules/process-template-editor/graph-utils'
import ProcessChart from '../../../modules/process-template-editor/components/ProcessChart'
import type { TaskTemplateType } from '../../../modules/datatypes/Task'
import ButtonWithDialog from '../../../modules/common/components/ButtonWithDialog'
import ProcessApi from '../../../modules/api/ProcessApi'

const CustomLink: StyledComponent<{}, {}, *> = styled(Link)`
  margin: 5px;
  color: black;
  &:hover {
    text-decoration: none;
    color: black;
  }
  > * {
    padding-bottom: 0;
  }
`
const Item: StyledComponent<{}, {}, *> = styled('div')`
  display: flex;
  flex-direction: row;
  align-items: center;
  & > * {
    padding: 5px;
  }
`

const CustomButtonGroup = styled(ButtonGroup)`
  border-radius: 3px;
  padding: 0;
  margin: 10px 0;
  border: 1px solid ${Colors.LIGHT_GRAY4};
`

const AllTheWay: StyledComponent<{}, {}, *> = styled('div')`
  display: flex;
  flex-direction: row;
  align-items: center;
  margin: 0 -20px;
  margin-top: 10px;
  padding: 10px;
  background-color: ${Colors.LIGHT_GRAY4};
`

const preventDefault = (event: SyntheticMouseEvent<HTMLElement>) => event.preventDefault()

type PropsType = { template: ProcessTemplateType, users: Map<string, UserType>, refresh: () => void }

type StateType = { errorAlert: ?string, deleteLoading: boolean, duplicateLoading: boolean }

class ProcessTemplateCard extends React.Component<PropsType, StateType> {
  state = {
    errorAlert: null,
    deleteLoading: false,
    duplicateLoading: false
  }

  onDelete = async () => {
    try {
      this.setState({ deleteLoading: true })
      await new ProcessApi().deleteProcessTemplate(this.props.template.id)
      this.setState({ deleteLoading: false })
      this.props.refresh()
    } catch (error) {
      this.setState({
        errorAlert: error.message,
        deleteLoading: false
      })
    }
  }

  onDuplicate = async () => {
    const { description, title, taskTemplates, durationLimit, ownerId } = this.props.template
    try {
      this.setState({ duplicateLoading: true })
      const { newId } = await new ProcessApi().addProcessTemplate({
        description,
        taskTemplates,
        durationLimit,
        ownerId,
        title: `${title} (duplicate)`
      })
      this.setState({ duplicateLoading: false })
      navigate(`/process-templates/edit/${newId}`)
    } catch (error) {
      this.setState({
        errorAlert: error.message,
        duplicateLoading: false
      })
    }
  }

  closeErrorAlert = () => this.setState({ errorAlert: null })

  render () {
    const { users, template } = this.props
    const { deleteLoading, duplicateLoading, errorAlert } = this.state
    const owner = users.get(template.ownerId)
    const graph = calcGraph<number, TaskTemplateType>(template.taskTemplates)
    const criticalLength = Math.max(...graph.map(task => task.endDate))
    return <CustomLink to={`./edit/${template.id}`}>
      <Card style={{
        width: '390px',
        margin: '10px'
      }} interactive>
        <H3>{template.title}</H3>
        <Item><Icon icon='stopwatch'/><span>Duration Limit of {template.durationLimit}</span></Item>
        <Item><Icon icon='stopwatch'/><span>Critical Path of length {criticalLength}</span></Item>
        <Item><Icon
          icon='refresh'/><span>{template.runningProcesses} of {template.processCount} processes running</span></Item>
        <Item><Icon icon='time'/><span>Created at {template.createdAt.toLocaleString()}</span></Item>
        {owner && <Item><Icon icon='user'/><span>{`${owner.name} (${owner.displayname})`}</span></Item>}
        <Item>
          <CustomButtonGroup fill large minimal onClick={preventDefault}>
            <Button icon='play' fill intent='success'>Start...</Button>
            <Button icon='duplicate' fill intent='none' onClick={this.onDuplicate}
                    loading={duplicateLoading}>Duplicate</Button>
            <ButtonWithDialog icon='trash' fill intent='danger' text='Delete' loading={deleteLoading}
                              onClick={this.onDelete}>
              <H4>Delete Process Template?</H4>
              <p>
                Are you sure you want to delete this process template?
              </p>
            </ButtonWithDialog>
            <Alert isOpen={errorAlert !== null} onClose={this.closeErrorAlert}>{errorAlert}</Alert>
          </CustomButtonGroup>
        </Item>
        <AllTheWay><ProcessChart mini tasks={graph}/></AllTheWay>
      </Card>
    </CustomLink>
  }
}

export default ProcessTemplateCard
