package ecs.components;

import ecs.Quests.QuestTag;
import ecs.entities.Entity;

import java.util.ArrayList;

/**This component saves the affiliation of the entity with regard to the quests.
 *  If an entity is to be the target of a quest,
 *  the entity must receive the QuestTag of the respective quest.
 *  **/
public class QuestObjectiveComponent extends Component {

    private ArrayList<QuestTag> partOfQuests;
    /**
     * Create a new component and add it to the associated entity
     *
     * @param entity associated entity
     * @param partOfQuests List of QuestTags, this entity belongs to
     */
    public QuestObjectiveComponent(Entity entity,ArrayList<QuestTag> partOfQuests) {
        super(entity);
        this.partOfQuests = partOfQuests;
    }

    /**
     * Create a new component and add it to the associated entity
     *
     * @param entity associated entity
     * @param questTag questTag, this entity belongs to
     */
    public QuestObjectiveComponent(Entity entity,QuestTag questTag) {
        super(entity);
        partOfQuests = new ArrayList<>();
        this.partOfQuests.add(questTag);
    }

    /**
     * Get all QuestTags, this entity belongs to
     * @return List of QuestTags
     * **/
    public ArrayList<QuestTag> getPartOfQuests() {
        return partOfQuests;
    }

    /**
     * Add a new QuestTag for this entity
     * @param questTag the new QuestTag
     * **/
    public void addQuestTag(QuestTag questTag) {
        partOfQuests.add(questTag);
    }

    /**
     * Add new QuestTags for this entity
     * @param questTagList the new QuestTags
     * **/
    public void addQuestTagList(ArrayList<QuestTag> questTagList) {
        for(QuestTag q: questTagList) {
            addQuestTag(q);
        }
    }

    /**
     * Change the current List oof QuestTags to the new
     * @param questTagList the new QuestTags
     * **/
    public void changeQuestTagList(ArrayList<QuestTag> questTagList) {
        partOfQuests = questTagList;
    }
}
